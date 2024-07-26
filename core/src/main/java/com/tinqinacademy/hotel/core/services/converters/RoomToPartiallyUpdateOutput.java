package com.tinqinacademy.hotel.core.services.converters;


import com.tinqinacademy.hotel.api.operations.partiallyupdate.PartiallyUpdateOutput;
import com.tinqinacademy.hotel.api.operations.updateroom.UpdateRoomOutput;
import com.tinqinacademy.hotel.persistence.models.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Slf4j
@Component
public class RoomToPartiallyUpdateOutput implements Converter<Room, PartiallyUpdateOutput>{
    @Override
    public PartiallyUpdateOutput convert(Room input) {
        log.info("Start converter RoomToPartiallyUpdateOutput input = {}",input);
        PartiallyUpdateOutput output = PartiallyUpdateOutput.builder()
                .id(input.getId().toString())
                .build();
        log.info("End converter RoomToPartiallyUpdateOutput output = {}",output);
        return output;
    }
}
