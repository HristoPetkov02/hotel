package com.tinqinacademy.hotel.core.services.converters;

import com.tinqinacademy.hotel.core.services.base.BaseConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import com.tinqinacademy.hotel.api.model.BathroomType;
import com.tinqinacademy.hotel.api.operations.getroom.GetRoomOutput;
import com.tinqinacademy.hotel.persistence.models.Room;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RoomToGetRoomOutputBuilder extends BaseConverter<Room, GetRoomOutput.GetRoomOutputBuilder> {
    @Override
    public GetRoomOutput.GetRoomOutputBuilder convertObject(Room room) {
        GetRoomOutput.GetRoomOutputBuilder output = GetRoomOutput.builder()
                .id(String.valueOf(room.getId()))
                .price(room.getPrice())
                .floor(room.getFloorNumber())
                .bathroomType(BathroomType.getByCode(room.getBathroomType().toString()));
        return output;
    }
}
