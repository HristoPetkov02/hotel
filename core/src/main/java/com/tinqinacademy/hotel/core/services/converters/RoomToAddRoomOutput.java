package com.tinqinacademy.hotel.core.services.converters;


import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomOutput;
import com.tinqinacademy.hotel.core.services.base.BaseConverter;
import com.tinqinacademy.hotel.persistence.models.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
