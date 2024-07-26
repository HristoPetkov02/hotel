package com.tinqinacademy.hotel.core.services.converters;

import com.tinqinacademy.hotel.api.operations.addroom.AddRoomInput;
import com.tinqinacademy.hotel.persistence.models.Room;
import com.tinqinacademy.hotel.persistence.models.enums.BathroomType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AddRoomInputToRoomBuilder implements Converter<AddRoomInput, Room.RoomBuilder> {
    @Override
    public Room.RoomBuilder convert(AddRoomInput input) {
        log.info("Start converter AddRoomInputToRoomBuilder input = {}",input);
        Room.RoomBuilder output = Room.builder()
                .bathroomType(BathroomType.getByCode(input.getBathroomType()))
                .floorNumber(input.getFloor())
                .roomNumber(input.getRoomNo())
                .price(input.getPrice());
        log.info("End converter AddRoomInputToRoomBuilder output = {}",output);
        return output;
    }
}
