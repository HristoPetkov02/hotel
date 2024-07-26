package com.tinqinacademy.hotel.core.services.converters;

import com.tinqinacademy.hotel.api.operations.availablerooms.AvailableRoomsOutput;
import com.tinqinacademy.hotel.persistence.models.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class RoomsToAvailableRoomsOutput implements Converter<List<Room>, AvailableRoomsOutput> {
    @Override
    public AvailableRoomsOutput convert(List<Room> rooms) {
        log.info("Start converter RoomsToAvailableRoomsOutput input = {}",rooms);
        List<String> ids = rooms.stream().map(Room::getId).map(UUID::toString).toList();
        AvailableRoomsOutput output = AvailableRoomsOutput
                .builder()
                .ids(ids)
                .build();
        log.info("End converter RoomsToAvailableRoomsOutput output = {}",output);
        return output;
    }
}
