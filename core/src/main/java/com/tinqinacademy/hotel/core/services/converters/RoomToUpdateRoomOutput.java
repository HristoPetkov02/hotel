package com.tinqinacademy.hotel.core.services.converters;

import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomOutput;
import com.tinqinacademy.hotel.core.services.base.BaseConverter;
import com.tinqinacademy.hotel.persistence.models.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RoomToUpdateRoomOutput extends BaseConverter<Room, UpdateRoomOutput> {


    @Override
    public UpdateRoomOutput convertObject(Room input) {
        UpdateRoomOutput output = UpdateRoomOutput.builder()
                .id(input.getId().toString())
                .build();
        return output;
    }
}
