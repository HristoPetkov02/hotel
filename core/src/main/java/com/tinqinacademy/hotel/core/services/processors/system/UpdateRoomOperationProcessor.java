package com.tinqinacademy.hotel.core.services.processors.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.hotel.api.interfaces.ErrorHandlerService;
import com.tinqinacademy.hotel.api.model.BathroomType;
import com.tinqinacademy.hotel.api.model.ErrorWrapper;
import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomInput;
import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomOperation;
import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomOutput;
import com.tinqinacademy.hotel.core.services.base.BaseOperationProcessor;
import com.tinqinacademy.hotel.api.exceptions.HotelApiException;
import com.tinqinacademy.hotel.persistence.models.Bed;
import com.tinqinacademy.hotel.persistence.models.Room;
import com.tinqinacademy.hotel.persistence.models.enums.BedSize;
import com.tinqinacademy.hotel.persistence.repository.BedRepository;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class UpdateRoomOperationProcessor extends BaseOperationProcessor<UpdateRoomInput, UpdateRoomOutput> implements UpdateRoomOperation {
    private final RoomRepository roomRepository;
    private final BedRepository bedRepository;


    public UpdateRoomOperationProcessor(ConversionService conversionService, ObjectMapper mapper, ErrorHandlerService errorHandlerService, Validator validator, RoomRepository roomRepository, BedRepository bedRepository) {
        super(conversionService, mapper, errorHandlerService, validator);
        this.roomRepository = roomRepository;
        this.bedRepository = bedRepository;
    }

    @Override
    public Either<ErrorWrapper, UpdateRoomOutput> process(UpdateRoomInput input) {
        return Try.of(() -> updateRoom(input))
                .toEither()
                .mapLeft(errorHandlerService::handle);
    }




    private Room getCurrentRoom(UpdateRoomInput input) {
        Room currentRoom = roomRepository.findById(UUID.fromString(input.getRoomId()))
                .orElseThrow(() -> new HotelApiException(
                        String.format("Room with id %s not found", input.getRoomId()),
                        HttpStatus.NOT_FOUND));
        return currentRoom;
    }


    private void checkRoomNumberExists(UpdateRoomInput input) {
        if (BathroomType.getByCode(input.getBathroomType()).equals(BathroomType.UNKNOWN)
                && input.getBathroomType() != null) {
            throw new HotelApiException(
                    String.format("Bathroom type %s is not valid", input.getBathroomType()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private List<Bed> getBeds(UpdateRoomInput input){
        List<Bed> beds = new ArrayList<>();
        for (String code : input.getBedSizes()) {
            if (BedSize.getByCode(code).getCode().isEmpty())
                continue;
            beds.add(bedRepository.findBySize(BedSize.getByCode(code))
                    .orElseThrow(() -> new HotelApiException(
                            String.format("Bed size %s is not valid",code),
                            HttpStatus.BAD_REQUEST)));
        }
        return beds;
    }

    private void isRoomNumberUnique(Room currentRoom, Room updatedRoom){
        if (!Objects.equals(currentRoom.getRoomNumber(), updatedRoom.getRoomNumber()) && roomRepository.existsByRoomNumber(updatedRoom.getRoomNumber())) {
            throw new HotelApiException("Room number must be unique", HttpStatus.BAD_REQUEST);
        }
    }


    private void checkIfBedCountMatches(UpdateRoomInput input) {
        if (input.getBedCount() != input.getBedSizes().size()) {
            throw new HotelApiException("Bed count must match bed sizes", HttpStatus.BAD_REQUEST);
        }
    }


    private UpdateRoomOutput updateRoom(UpdateRoomInput input) {
        logStart(input);

        validateInput(input);
        checkIfBedCountMatches(input);

        Room currentRoom = getCurrentRoom(input);
        checkRoomNumberExists(input);

        List<Bed> beds = getBeds(input);

        Room updatedRoom = Objects.requireNonNull(conversionService.convert(input, Room.RoomBuilder.class))
                .beds(beds)
                .floorNumber(currentRoom.getFloorNumber())
                .createdOn(currentRoom.getCreatedOn())
                .build();

        isRoomNumberUnique(currentRoom,updatedRoom);

        roomRepository.save(updatedRoom);

        UpdateRoomOutput output = conversionService.convert(updatedRoom, UpdateRoomOutput.class);
        logEnd(output);
        return output;
    }
}
