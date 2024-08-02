package com.tinqinacademy.hotel.core.services.converters;


import com.tinqinacademy.hotel.api.operations.addroom.AddRoomOutput;
import com.tinqinacademy.hotel.core.services.base.BaseConverter;
import com.tinqinacademy.hotel.persistence.models.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class RoomToAddRoomOutput extends BaseConverter<Room, AddRoomOutput> {
    @Override
    public AddRoomOutput convertObject(Room input) {
        AddRoomOutput output = AddRoomOutput.builder()
                .id(input.getId().toString())
                .build();
        return output;
    }
}
