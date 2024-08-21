package com.tinqinacademy.hotel.core.services.converters;

import com.tinqinacademy.hotel.api.operations.hotel.getbyroomnumber.GetByRoomNumberOutput;
import com.tinqinacademy.hotel.core.services.base.BaseConverter;
import com.tinqinacademy.hotel.persistence.models.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomToGetByRoomNumberOutput extends BaseConverter<Room, GetByRoomNumberOutput> {
    @Override
    protected GetByRoomNumberOutput convertObject(Room input) {
        GetByRoomNumberOutput output = GetByRoomNumberOutput.builder()
                .roomId(String.valueOf(input.getId()))
                .build();
        return output;
    }
}
