package com.tinqinacademy.hotel.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomInput;
import com.tinqinacademy.hotel.api.restroutes.RestApiRoutes;
import com.tinqinacademy.hotel.persistence.models.Bed;
import com.tinqinacademy.hotel.persistence.models.Booking;
import com.tinqinacademy.hotel.persistence.models.Room;
import com.tinqinacademy.hotel.persistence.models.enums.BathroomType;
import com.tinqinacademy.hotel.persistence.models.enums.BedSize;
import com.tinqinacademy.hotel.persistence.repository.BedRepository;
import com.tinqinacademy.hotel.persistence.repository.BookingRepository;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY, connection = EmbeddedDatabaseConnection.H2)
public class HotelControllerTests {
    private final MockMvc mvc;
    private final RoomRepository roomRepository;
    private final ObjectMapper objectMapper;
    private final BedRepository bedRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public HotelControllerTests(MockMvc mvc, RoomRepository roomRepository, ObjectMapper objectMapper, BedRepository bedRepository, BookingRepository bookingRepository) {
        this.mvc = mvc;
        this.roomRepository = roomRepository;
        this.objectMapper = objectMapper;
        this.bedRepository = bedRepository;
        this.bookingRepository = bookingRepository;
    }

    @BeforeEach
    public void setup() {

        Bed bed = bedRepository.findBySize(BedSize.SINGLE).orElseGet(() -> {
            Bed newBed = Bed.builder()
                    .bedSize(BedSize.SINGLE)
                    .capacity(1)
                    .build();
            return bedRepository.save(newBed);
        });

        Room room = Room.builder()
                .roomNumber("101")
                .bathroomType(BathroomType.SHARED)
                .beds(List.of(bed))
                .price(BigDecimal.valueOf(678.32))
                .floorNumber(3)
                .price(BigDecimal.valueOf(1))
                .build();

        room = roomRepository.save(room);

        Booking booking = Booking.builder()
                .startDate(LocalDate.of(2024, 5, 25))
                .endDate(LocalDate.of(2024, 5, 27))
                .room(room)
                .userId(UUID.randomUUID())
                .totalPrice(BigDecimal.valueOf(1))
                .guests(new HashSet<>())
                .build();

        bookingRepository.save(booking);
    }

    @AfterEach
    public void clearDB() {
        bookingRepository.deleteAll();
        roomRepository.deleteAll();
    }


    @Test
    public void testGetRoomOk() throws Exception {
        Room room = roomRepository.findRoomByRoomNumber("101").orElseThrow();
        mvc.perform(get(RestApiRoutes.API_HOTEL_GET_ROOM, room.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetRoomNotFound() throws Exception {
        UUID uuid = UUID.randomUUID();
        Room room = roomRepository.findRoomByRoomNumber("101").orElseThrow();
        while (room.getId().equals(uuid)) {
            uuid = UUID.randomUUID();
        }
        mvc.perform(get(RestApiRoutes.API_HOTEL_GET_ROOM, UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetRoomBadRequest() throws Exception {
        mvc.perform(get(RestApiRoutes.API_HOTEL_GET_ROOM, "uuid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }





    @Test
    public void testCheckAvailabilityOk() throws Exception {
        mvc.perform(get(RestApiRoutes.API_HOTEL_CHECK_AVAILABILITY)
                        .param("startDate", "2024-11-11")
                        .param("endDate", "2025-11-11")
                        .param("bedCount", "1")
                        .param("bedSize", "single")
                        .param("bathroomType", "shared")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testCheckAvailabilityNotFound() throws Exception {
        mvc.perform(get(RestApiRoutes.API_HOTEL_CHECK_AVAILABILITY)
                        .param("startDate", "2024-11-11")
                        .param("endDate", "2025-11-11")
                        .param("bedCount", "2")
                        .param("bedSize", "single")
                        .param("bathroomType", "private")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCheckAvailabilityBadRequest() throws Exception {
        mvc.perform(get(RestApiRoutes.API_HOTEL_CHECK_AVAILABILITY)
                        .param("bedCount", "2")
                        .param("bedSize", "single")
                        .param("bathroomType", "shared")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }



    @Test
    public void testBookRoomCreated() throws Exception {
        BookRoomInput bookRoomInput = BookRoomInput.builder()
                .startDate(LocalDate.of(2024, 11, 25))
                .endDate(LocalDate.of(2024, 11, 27))
                .userId(UUID.randomUUID().toString())
                .build();
        String json = objectMapper.writeValueAsString(bookRoomInput);
        Room room = roomRepository.findRoomByRoomNumber("101").orElseThrow();
        mvc.perform(post(RestApiRoutes.API_HOTEL_BOOK_ROOM, room.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))

                .andExpect(status().isCreated());
    }

    @Test
    public void testBookRoomNotFound() throws Exception {
        BookRoomInput bookRoomInput = BookRoomInput.builder()
                .startDate(LocalDate.of(2024, 11, 25))
                .endDate(LocalDate.of(2024, 11, 27))
                .userId(UUID.randomUUID().toString())
                .build();
        UUID uuid = UUID.randomUUID();
        Room room = roomRepository.findRoomByRoomNumber("101").orElseThrow();
        while (room.getId().equals(uuid)) {
            uuid = UUID.randomUUID();
        }
        String json = objectMapper.writeValueAsString(bookRoomInput);
        mvc.perform(post(RestApiRoutes.API_HOTEL_BOOK_ROOM, uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testBookRoomBadRequest() throws Exception {
        BookRoomInput bookRoomInput = BookRoomInput.builder()
                .build();
        String json = objectMapper.writeValueAsString(bookRoomInput);
        mvc.perform(post(RestApiRoutes.API_HOTEL_BOOK_ROOM, "uuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }



    @Test
    public void testUnbookRoomOk() throws Exception {
        Room room = roomRepository.findRoomByRoomNumber("101").orElseThrow();
        Booking booking = bookingRepository.findAllBookingsByRoomId(room.getId()).orElseThrow().getFirst();
        UnbookRoomInput unbookRoomInput = UnbookRoomInput.builder()
                .userId(booking.getUserId().toString())
                .build();

        String json = objectMapper.writeValueAsString(unbookRoomInput);
        mvc.perform(delete(RestApiRoutes.API_HOTEL_UNBOOK_ROOM, booking.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void testUnbookRoomNotFound() throws Exception {
        UUID uuid = UUID.randomUUID();
        Room room = roomRepository.findRoomByRoomNumber("101").orElseThrow();
        Booking booking = bookingRepository.findAllBookingsByRoomId(room.getId()).orElseThrow().getFirst();
        while (booking.getId().equals(uuid)) {
            uuid = UUID.randomUUID();
        }
        UnbookRoomInput unbookRoomInput = UnbookRoomInput.builder()
                .userId(booking.getUserId().toString())
                .build();

        String json = objectMapper.writeValueAsString(unbookRoomInput);
        mvc.perform(delete(RestApiRoutes.API_HOTEL_UNBOOK_ROOM, uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testUnbookRoomBadRequest() throws Exception {
        UnbookRoomInput unbookRoomInput = UnbookRoomInput.builder()
                .build();
        String json = objectMapper.writeValueAsString(unbookRoomInput);
        mvc.perform(delete(RestApiRoutes.API_HOTEL_UNBOOK_ROOM, "uuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }



    @Test
    public void testGetRoomByRoomNumberOk() throws Exception {
        Room room = roomRepository.findRoomByRoomNumber("101").orElseThrow();
        mvc.perform(get(RestApiRoutes.API_HOTEL_GET_ROOM_BY_ROOM_NUMBER, room.getRoomNumber())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetRoomByRoomNumberNotFound() throws Exception {
        mvc.perform(get(RestApiRoutes.API_HOTEL_GET_ROOM_BY_ROOM_NUMBER, "102")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetRoomByRoomNumberBadRequest() throws Exception {
        mvc.perform(get(RestApiRoutes.API_HOTEL_GET_ROOM_BY_ROOM_NUMBER, "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
