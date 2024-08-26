package com.tinqinacademy.hotel.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.hotel.api.model.input.VisitorRegisterInput;
import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomInput;
import com.tinqinacademy.hotel.api.operations.system.registervisitors.RegisterVisitorsInput;
import com.tinqinacademy.hotel.api.restroutes.RestApiRoutes;
import com.tinqinacademy.hotel.persistence.models.Bed;
import com.tinqinacademy.hotel.persistence.models.Booking;
import com.tinqinacademy.hotel.persistence.models.Room;
import com.tinqinacademy.hotel.persistence.models.enums.BathroomType;
import com.tinqinacademy.hotel.persistence.models.enums.BedSize;
import com.tinqinacademy.hotel.persistence.repository.BedRepository;
import com.tinqinacademy.hotel.persistence.repository.BookingRepository;
import com.tinqinacademy.hotel.persistence.repository.GuestRepository;
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
public class SystemControllerTests {
    private final MockMvc mvc;
    private final ObjectMapper objectMapper;
    private final RoomRepository roomRepository;
    private final BedRepository bedRepository;
    private final BookingRepository bookingRepository;
    private final GuestRepository guestRepository;

    @Autowired
    public SystemControllerTests(MockMvc mvc, ObjectMapper objectMapper, RoomRepository roomRepository, BedRepository bedRepository, BookingRepository bookingRepository, GuestRepository guestRepository) {
        this.mvc = mvc;
        this.objectMapper = objectMapper;
        this.roomRepository = roomRepository;
        this.bedRepository = bedRepository;
        this.bookingRepository = bookingRepository;
        this.guestRepository = guestRepository;
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
                .roomNumber("delete")
                .bathroomType(BathroomType.SHARED)
                .beds(List.of(bed))
                .price(BigDecimal.valueOf(678.32))
                .floorNumber(3)
                .price(BigDecimal.valueOf(1))
                .build();

        roomRepository.save(room);

        room = Room.builder()
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
        guestRepository.deleteAll();
    }


    @Test
    public void testAddRoomCreated() throws Exception {
        AddRoomInput input = AddRoomInput.builder()
                .bedCount(1)
                .bedSizes(List.of(BedSize.SINGLE.toString()))
                .bathroomType(BathroomType.SHARED.toString())
                .floor(3)
                .roomNo("102")
                .price(BigDecimal.valueOf(678.32))
                .build();
        String json = objectMapper.writeValueAsString(input);
        mvc.perform(post(RestApiRoutes.API_SYSTEM_ADD_ROOM)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    public void testAddRoomBadRequest() throws Exception {
        //bad request for adding already existing room
        AddRoomInput input = AddRoomInput.builder()
                .bedCount(1)
                .bedSizes(List.of(BedSize.SINGLE.toString()))
                .bathroomType(BathroomType.SHARED.toString())
                .floor(3)
                .roomNo("101")
                .price(BigDecimal.valueOf(678.32))
                .build();
        String json = objectMapper.writeValueAsString(input);
        mvc.perform(post(RestApiRoutes.API_SYSTEM_ADD_ROOM)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());


        //bad request in validation
        input = AddRoomInput.builder()
                .bedCount(1)
                .floor(-10)
                .roomNo("101")
                .build();
        json = objectMapper.writeValueAsString(input);
        mvc.perform(post(RestApiRoutes.API_SYSTEM_ADD_ROOM)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testRegisterVisitorsCreated() throws Exception {
        List<VisitorRegisterInput> visitors = List.of(
                VisitorRegisterInput.builder()
                        .startDate(LocalDate.of(2024, 5, 25))
                        .endDate(LocalDate.of(2024, 5, 27))
                        .firstName("Пепи")
                        .lastName("Пупи")
                        .phoneNo("0888888")
                        .birthDate(LocalDate.of(2012, 3, 3))
                        .idCardNo("id card")
                        .idCardIssueAthority("id authority")
                        .idCardValidity(LocalDate.now().plusDays(5))
                        .idCardNo("id card number")
                        .roomNo("101")
                        .build(),
                VisitorRegisterInput.builder()
                        .startDate(LocalDate.of(2024, 5, 25))
                        .endDate(LocalDate.of(2024, 5, 27))
                        .firstName("Пепи")
                        .lastName("Пупи")
                        .phoneNo("0888888")
                        .birthDate(LocalDate.of(2012, 3, 3))
                        .roomNo("101")
                        .build()
        );

        RegisterVisitorsInput input = RegisterVisitorsInput.builder()
                .visitorRegisterInputs(visitors)
                .build();

        String json = objectMapper.writeValueAsString(input);
        mvc.perform(post(RestApiRoutes.API_SYSTEM_REGISTER_VISITOR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    public void testRegisterVisitorsBadRequest() throws Exception {
        List<VisitorRegisterInput> visitors = List.of(
                VisitorRegisterInput.builder()
                        .phoneNo("0888")
                        .birthDate(LocalDate.of(2012, 3, 3))
                        .roomNo("102")
                        .build()
        );

        RegisterVisitorsInput input = RegisterVisitorsInput.builder()
                .visitorRegisterInputs(visitors)
                .build();

        String json = objectMapper.writeValueAsString(input);
        mvc.perform(post(RestApiRoutes.API_SYSTEM_REGISTER_VISITOR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegisterVisitorsNotFound() throws Exception{
        List<VisitorRegisterInput> visitors = List.of(
                VisitorRegisterInput.builder()
                        .startDate(LocalDate.of(2024, 11, 25))
                        .endDate(LocalDate.of(2024, 11, 27))
                        .firstName("Пепи")
                        .lastName("Пупи")
                        .phoneNo("0888888")
                        .birthDate(LocalDate.of(2012, 3, 3))
                        .roomNo("122")
                        .build()
        );

        RegisterVisitorsInput input = RegisterVisitorsInput.builder()
                .visitorRegisterInputs(visitors)
                .build();

        String json = objectMapper.writeValueAsString(input);
        mvc.perform(post(RestApiRoutes.API_SYSTEM_REGISTER_VISITOR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }



    @Test
    public void testReportByCriteriaOk() throws Exception {
        mvc.perform(get(RestApiRoutes.API_SYSTEM_VISITOR_REPORT)
                        .param("startDate", "2024-05-25")
                        .param("endDate", "2024-05-27")
                        .param("firstName", "Пепи")
                        .param("lastName", "Пупи"))
                .andExpect(status().isOk());
    }

    @Test
    public void testReportByCriteriaBadRequest() throws Exception {
        mvc.perform(get(RestApiRoutes.API_SYSTEM_VISITOR_REPORT)
                        .param("startDate", "2024-05-25")
                        .param("roomNo", "101"))
                .andExpect(status().isBadRequest());
    }



    @Test
    public void testDeleteRoomOk() throws Exception {
        Room room = roomRepository.findRoomByRoomNumber("delete").orElseThrow();
        mvc.perform(delete(RestApiRoutes.API_SYSTEM_DELETE_ROOM, room.getId().toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteRoomBadRequest() throws Exception {
        mvc.perform(delete(RestApiRoutes.API_SYSTEM_DELETE_ROOM, "uuid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteRoomNotFound() throws Exception {
        mvc.perform(delete(RestApiRoutes.API_SYSTEM_DELETE_ROOM, UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }
}
