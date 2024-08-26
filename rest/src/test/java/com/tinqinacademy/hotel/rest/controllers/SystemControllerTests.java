package com.tinqinacademy.hotel.rest.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomInput;
import com.tinqinacademy.hotel.api.restroutes.RestApiRoutes;
import com.tinqinacademy.hotel.persistence.models.Bed;
import com.tinqinacademy.hotel.persistence.models.Room;
import com.tinqinacademy.hotel.persistence.models.enums.BathroomType;
import com.tinqinacademy.hotel.persistence.models.enums.BedSize;
import com.tinqinacademy.hotel.persistence.repository.BedRepository;
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
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Autowired
    public SystemControllerTests(MockMvc mvc, ObjectMapper objectMapper, RoomRepository roomRepository, BedRepository bedRepository) {
        this.mvc = mvc;
        this.objectMapper = objectMapper;
        this.roomRepository = roomRepository;
        this.bedRepository = bedRepository;
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

        roomRepository.save(room);
    }

    @AfterEach
    public void clearDB() {
        roomRepository.deleteAll();
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


}
