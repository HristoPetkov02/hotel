package com.tinqinacademy.hotel.core.services.services;

import com.tinqinacademy.hotel.api.interfaces.ErrorHandlerService;
import com.tinqinacademy.hotel.api.model.BathroomType;
import com.tinqinacademy.hotel.api.model.ErrorWrapper;
import com.tinqinacademy.hotel.api.operations.addroom.AddRoomInput;
import com.tinqinacademy.hotel.api.operations.addroom.AddRoomOperation;
import com.tinqinacademy.hotel.api.operations.addroom.AddRoomOutput;
import com.tinqinacademy.hotel.core.services.exceptions.HotelApiException;
import com.tinqinacademy.hotel.persistence.models.Bed;
import com.tinqinacademy.hotel.persistence.models.Room;
import com.tinqinacademy.hotel.persistence.models.enums.BedSize;
import com.tinqinacademy.hotel.persistence.repository.BedRepository;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddRoomOperationProcessor implements AddRoomOperation {
    private final RoomRepository roomRepository;
    private final BedRepository bedRepository;
    private final ConversionService conversionService;
    private final ErrorHandlerService errorHandlerService;



    @Override
    public Either<ErrorWrapper, AddRoomOutput> process(AddRoomInput input) {
        return Try.of(() -> addRoom(input))
                .toEither()
                .mapLeft(errorHandlerService::handle);
    }



    private void checkRoomNumberExists(AddRoomInput input) {
            if (roomRepository.existsByRoomNumber(input.getRoomNo())) {
                throw new HotelApiException("Room number already exists", HttpStatus.BAD_REQUEST);
            }
    }



    private void checkBedCount(AddRoomInput input) {
        if (input.getBedSizes().size() != input.getBedCount()) {
            throw new HotelApiException("Wrong bed count", HttpStatus.BAD_REQUEST);
        }
    }



    private void checkBathroomType(AddRoomInput input) {
        if (BathroomType.getByCode(input.getBathroomType()).toString().isEmpty()) {
            throw new HotelApiException("Wrong bathroom type", HttpStatus.BAD_REQUEST);
        }
    }



    private List<Bed> getBeds(AddRoomInput input) {
        List<Bed> beds = input.getBedSizes().stream()
                .filter(code -> !BedSize.getByCode(code).getCode().isEmpty())
                .map(code -> bedRepository.findBySize(BedSize.getByCode(code))
                        .orElseThrow(() -> new HotelApiException("Wrong bed", HttpStatus.BAD_REQUEST)))
                .collect(Collectors.toList());
        return beds;
    }



    private void validateInput(AddRoomInput input){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<AddRoomInput>> violations = validator.validate(input);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }



    private AddRoomOutput addRoom(AddRoomInput input) {
        log.info("Start addRoom input = {}", input);

        validateInput(input);

        checkRoomNumberExists(input);
        checkBedCount(input);
        checkBathroomType(input);

        List<Bed> beds = getBeds(input);

        Room room = Objects.requireNonNull(conversionService.convert(input, Room.RoomBuilder.class))
                .beds(beds)
                .build();

        roomRepository.save(room);

        AddRoomOutput output = conversionService.convert(room, AddRoomOutput.class);

        log.info("End addRoom output = {}", output);
        return output;
    }
}
