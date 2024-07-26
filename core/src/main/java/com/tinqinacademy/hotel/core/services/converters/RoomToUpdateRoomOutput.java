package com.tinqinacademy.hotel.core.services.converters;

import com.tinqinacademy.hotel.api.operations.addroom.AddRoomOutput;
import com.tinqinacademy.hotel.api.operations.updateroom.UpdateRoomOutput;
import com.tinqinacademy.hotel.persistence.models.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RoomToUpdateRoomOutput implements Converter<Room, UpdateRoomOutput> {


    @Override
    public UpdateRoomOutput convert(Room input) {
        log.info("Start converter RoomToAddRoomOutput input = {}",input);
        UpdateRoomOutput output = UpdateRoomOutput.builder()
                .id(input.getId().toString())
                .build();
        log.info("End converter RoomToAddRoomOutput output = {}",output);
        return output;
    }
}
