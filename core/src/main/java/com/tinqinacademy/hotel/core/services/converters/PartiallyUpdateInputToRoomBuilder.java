package com.tinqinacademy.hotel.core.services.converters;

import com.tinqinacademy.hotel.api.operations.partiallyupdate.PartiallyUpdateInput;
import com.tinqinacademy.hotel.persistence.models.Room;
import com.tinqinacademy.hotel.persistence.models.enums.BathroomType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import org.springframework.core.convert.converter.Converter;

@Component
@Slf4j
public class PartiallyUpdateInputToRoomBuilder implements Converter<PartiallyUpdateInput, Room.RoomBuilder>{
    @Override
    public Room.RoomBuilder convert(PartiallyUpdateInput input) {
        log.info("Start converter PartiallyUpdateInputToRoomBuilder input = {}",input);
        Room.RoomBuilder roomBuilder = Room.builder()
                .bathroomType(BathroomType.getByCode(input.getBathroomType()))
                .roomNumber(input.getRoomNo())
                .price(input.getPrice());
        log.info("End converter PartiallyUpdateInputToRoomBuilder output = {}",roomBuilder);
        return roomBuilder;
    }
}
