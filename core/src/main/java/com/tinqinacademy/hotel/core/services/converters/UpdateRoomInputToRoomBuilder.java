package com.tinqinacademy.hotel.core.services.converters;

import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomInput;
import com.tinqinacademy.hotel.core.services.base.BaseConverter;
import com.tinqinacademy.hotel.persistence.models.Room;
import com.tinqinacademy.hotel.persistence.models.enums.BathroomType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class UpdateRoomInputToRoomBuilder extends BaseConverter<UpdateRoomInput, Room.RoomBuilder> {
    @Override
    public Room.RoomBuilder convertObject(UpdateRoomInput input) {
        Room.RoomBuilder output = Room.builder()
                .id(UUID.fromString(input.getRoomId()))
                .bathroomType(BathroomType.getByCode(input.getBathroomType()))
                .roomNumber(input.getRoomNo())
                .price(input.getPrice());
        return output;
    }
}
