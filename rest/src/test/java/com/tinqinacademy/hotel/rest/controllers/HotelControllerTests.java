package com.tinqinacademy.hotel.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY, connection = EmbeddedDatabaseConnection.H2)
public class HotelControllerTests {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BedRepository bedRepository;
    @Autowired
    private BookingRepository bookingRepository;


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
    public void testCheckAvailableRoomsOk() throws Exception {
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
    public void testCheckAvailableRoomsNotFound() throws Exception {
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
    public void testCheckAvailableRoomsBadRequest() throws Exception {
        mvc.perform(get(RestApiRoutes.API_HOTEL_CHECK_AVAILABILITY)
                        .param("bedCount", "2")
                        .param("bedSize", "single")
                        .param("bathroomType", "shared")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }





}
