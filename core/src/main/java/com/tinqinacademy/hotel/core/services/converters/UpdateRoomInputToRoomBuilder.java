package com.tinqinacademy.hotel.core.services.converters;

import com.tinqinacademy.hotel.api.operations.updateroom.UpdateRoomInput;
import com.tinqinacademy.hotel.persistence.models.Room;
import com.tinqinacademy.hotel.persistence.models.enums.BathroomType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class UpdateRoomInputToRoomBuilder implements Converter<UpdateRoomInput, Room.RoomBuilder> {
    @Override
    public Room.RoomBuilder convert(UpdateRoomInput input) {
        log.info("Start converter UpdateRoomInputToRoomBuilder input = {}",input);
        Room.RoomBuilder output = Room.builder()
                .id(UUID.fromString(input.getRoomId()))
                .bathroomType(BathroomType.getByCode(input.getBathroomType()))
                .roomNumber(input.getRoomNo())
                .price(input.getPrice());
        log.info("End converter UpdateRoomInputToRoomBuilder output = {}",output);
        return output;
    }
}
