package com.tinqinacademy.hotel.core.converters;

import com.tinqinacademy.hotel.api.operations.availablerooms.AvailableRoomsOutput;
import com.tinqinacademy.hotel.core.base.BaseConverter;
import com.tinqinacademy.hotel.persistence.models.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class RoomsToAvailableRoomsOutput extends BaseConverter<List<Room>, AvailableRoomsOutput> {
    @Override
    public AvailableRoomsOutput convertObject(List<Room> rooms) {
        List<String> ids = rooms.stream().map(Room::getId).map(UUID::toString).toList();
        AvailableRoomsOutput output = AvailableRoomsOutput
                .builder()
                .ids(ids)
                .build();
        return output;
    }
}
