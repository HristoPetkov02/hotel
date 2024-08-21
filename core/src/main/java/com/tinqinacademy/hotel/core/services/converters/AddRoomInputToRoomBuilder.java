package com.tinqinacademy.hotel.core.services.converters;

import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomInput;
import com.tinqinacademy.hotel.core.services.base.BaseConverter;
import com.tinqinacademy.hotel.persistence.models.Room;
import com.tinqinacademy.hotel.persistence.models.enums.BathroomType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AddRoomInputToRoomBuilder extends BaseConverter<AddRoomInput, Room.RoomBuilder> {
    @Override
    public Room.RoomBuilder convertObject(AddRoomInput input) {
        Room.RoomBuilder output = Room.builder()
                .bathroomType(BathroomType.getByCode(input.getBathroomType()))
                .floorNumber(input.getFloor())
                .roomNumber(input.getRoomNo())
                .price(input.getPrice());
        return output;
    }
}
