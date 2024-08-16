package com.tinqinacademy.hotel.core.services.processors.hotel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.hotel.api.interfaces.ErrorHandlerService;
import com.tinqinacademy.hotel.api.model.ErrorWrapper;
import com.tinqinacademy.hotel.api.operations.hotel.availablerooms.AvailableRoomsInput;
import com.tinqinacademy.hotel.api.operations.hotel.availablerooms.AvailableRoomsOperation;
import com.tinqinacademy.hotel.api.operations.hotel.availablerooms.AvailableRoomsOutput;
import com.tinqinacademy.hotel.core.services.base.BaseOperationProcessor;
import com.tinqinacademy.hotel.api.exceptions.HotelApiException;
import com.tinqinacademy.hotel.persistence.models.Room;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AvailableRoomsOperationProcessor extends BaseOperationProcessor<AvailableRoomsInput, AvailableRoomsOutput> implements AvailableRoomsOperation {
    private final RoomRepository roomRepository;


    public AvailableRoomsOperationProcessor(ConversionService conversionService, ObjectMapper mapper, ErrorHandlerService errorHandlerService, Validator validator, RoomRepository roomRepository) {
        super(conversionService, mapper, errorHandlerService, validator);
        this.roomRepository = roomRepository;
    }

    @Override
    public Either<ErrorWrapper, AvailableRoomsOutput> process(AvailableRoomsInput input) {
        return Try.of(() -> checkAvailableRooms(input))
                .toEither()
                .mapLeft(errorHandlerService::handle);
    }

    private List<Room> getAvailableRooms(AvailableRoomsInput input) {
        return roomRepository.findAvailableRooms(input.getStartDate(), input.getEndDate())
                .stream()
                .filter(room -> Optional.ofNullable(input.getBedCount()).map(count -> room.getBeds().size() == count).orElse(true))
                .filter(room -> Optional.ofNullable(input.getBedSize()).map(size -> room.getBeds().stream().anyMatch(bed -> bed.getBedSize().toString().equals(size))).orElse(true))
                .filter(room -> Optional.ofNullable(input.getBathroomType()).map(type -> room.getBathroomType().toString().equals(type)).orElse(true))
                .toList();
    }

    private void checkStartDateBeforeEndDate(AvailableRoomsInput input) {
        if (input.getStartDate().isAfter(input.getEndDate()))
            throw new HotelApiException("Start date must be before end date", HttpStatus.BAD_REQUEST);
    }

    private AvailableRoomsOutput checkAvailableRooms(AvailableRoomsInput input) {
        logStart(input);

        validateInput(input);
        checkStartDateBeforeEndDate(input);

        List<Room> rooms = getAvailableRooms(input);

        if (rooms.isEmpty())
            throw new HotelApiException("No rooms available", HttpStatus.NOT_FOUND);

        AvailableRoomsOutput output = conversionService.convert(rooms, AvailableRoomsOutput.class);
        logEnd(output);
        return output;
    }
}
