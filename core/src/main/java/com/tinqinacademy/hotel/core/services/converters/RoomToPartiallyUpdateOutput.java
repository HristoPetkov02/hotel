package com.tinqinacademy.hotel.core.services.converters;


import com.tinqinacademy.hotel.api.operations.partiallyupdate.PartiallyUpdateOutput;
import com.tinqinacademy.hotel.api.operations.updateroom.UpdateRoomOutput;
import com.tinqinacademy.hotel.core.services.base.BaseConverter;
import com.tinqinacademy.hotel.persistence.models.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Slf4j
@Component
public class RoomToPartiallyUpdateOutput extends BaseConverter<Room, PartiallyUpdateOutput> {
    @Override
    public PartiallyUpdateOutput convertObject(Room input) {
        PartiallyUpdateOutput output = PartiallyUpdateOutput.builder()
                .id(input.getId().toString())
                .build();
        return output;
    }
}
