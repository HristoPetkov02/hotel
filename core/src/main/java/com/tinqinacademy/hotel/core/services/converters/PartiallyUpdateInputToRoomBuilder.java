package com.tinqinacademy.hotel.core.services.converters;

import com.tinqinacademy.hotel.api.operations.partiallyupdate.PartiallyUpdateInput;
import com.tinqinacademy.hotel.core.services.base.BaseConverter;
import com.tinqinacademy.hotel.persistence.models.Room;
import com.tinqinacademy.hotel.persistence.models.enums.BathroomType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import org.springframework.core.convert.converter.Converter;

@Component
@Slf4j
public class PartiallyUpdateInputToRoomBuilder extends BaseConverter<PartiallyUpdateInput, Room.RoomBuilder> {
    @Override
    public Room.RoomBuilder convertObject(PartiallyUpdateInput input) {
        Room.RoomBuilder roomBuilder = Room.builder()
                .bathroomType(input.getBathroomType() != null ? BathroomType.getByCode(input.getBathroomType()) : null)
                .roomNumber(input.getRoomNo())
                .price(input.getPrice());
        return roomBuilder;
    }
}
