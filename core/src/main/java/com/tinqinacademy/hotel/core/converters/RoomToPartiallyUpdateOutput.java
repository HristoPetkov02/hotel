package com.tinqinacademy.hotel.core.converters;


import com.tinqinacademy.hotel.api.operations.partiallyupdate.PartiallyUpdateOutput;
import com.tinqinacademy.hotel.core.base.BaseConverter;
import com.tinqinacademy.hotel.persistence.models.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
