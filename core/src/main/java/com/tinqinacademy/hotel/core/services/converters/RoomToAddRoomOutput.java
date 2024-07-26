package com.tinqinacademy.hotel.core.services.converters;


import com.tinqinacademy.hotel.api.operations.addroom.AddRoomOutput;
import com.tinqinacademy.hotel.persistence.models.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class RoomToAddRoomOutput implements Converter<Room, AddRoomOutput>{
    @Override
    public AddRoomOutput convert(Room input) {
        log.info("Start converter RoomToAddRoomOutput input = {}",input);
        AddRoomOutput output = AddRoomOutput.builder()
                .id(input.getId().toString())
                .build();
        log.info("End converter RoomToAddRoomOutput output = {}",output);
        return output;
    }
}
