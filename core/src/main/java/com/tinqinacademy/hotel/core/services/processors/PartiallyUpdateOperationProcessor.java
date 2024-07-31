package com.tinqinacademy.hotel.core.services.processors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.tinqinacademy.hotel.api.interfaces.ErrorHandlerService;
import com.tinqinacademy.hotel.api.model.BathroomType;
import com.tinqinacademy.hotel.api.model.ErrorWrapper;
import com.tinqinacademy.hotel.api.operations.partiallyupdate.PartiallyUpdateInput;
import com.tinqinacademy.hotel.api.operations.partiallyupdate.PartiallyUpdateOperation;
import com.tinqinacademy.hotel.api.operations.partiallyupdate.PartiallyUpdateOutput;
import com.tinqinacademy.hotel.core.services.base.BaseOperationProcessor;
import com.tinqinacademy.hotel.core.services.exceptions.HotelApiException;
import com.tinqinacademy.hotel.persistence.models.Room;
import com.tinqinacademy.hotel.persistence.models.enums.BedSize;
import com.tinqinacademy.hotel.persistence.repository.BedRepository;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class PartiallyUpdateOperationProcessor extends BaseOperationProcessor<PartiallyUpdateInput, PartiallyUpdateOutput> implements PartiallyUpdateOperation {
    private final RoomRepository roomRepository;
    private final BedRepository bedRepository;


    public PartiallyUpdateOperationProcessor(ConversionService conversionService, ObjectMapper mapper, ErrorHandlerService errorHandlerService, Validator validator, RoomRepository roomRepository, BedRepository bedRepository) {
        super(conversionService, mapper, errorHandlerService, validator);
        this.roomRepository = roomRepository;
        this.bedRepository = bedRepository;
    }

    @Override
    public Either<ErrorWrapper, PartiallyUpdateOutput> process(PartiallyUpdateInput input) {
        return Try.of(() -> partiallyUpdate(input))
                .toEither()
                .mapLeft(errorHandlerService::handle);
    }

    private void checkBedCount(PartiallyUpdateInput input){
        if (input.getBedSizes() != null && input.getBedSizes().size() != input.getBedCount()) {
            throw new HotelApiException(
                    "Bed count must be equal to bed sizes count",
                    HttpStatus.BAD_REQUEST);
        }
    }

    private void checkBathroomType(PartiallyUpdateInput input){
        if (BathroomType.getByCode(input.getBathroomType()).equals(BathroomType.UNKNOWN)
                && input.getBathroomType() != null) {
            throw new HotelApiException(
                    String.format("Bathroom type %s not found", input.getBathroomType()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private Room getCurrentRoom(PartiallyUpdateInput input){
        Room currentRoom = roomRepository.findById(UUID.fromString(input.getRoomId()))
                .orElseThrow(() -> new HotelApiException(
                        String.format("Room with id %s not found", input.getRoomId()),
                        HttpStatus.NOT_FOUND
                ));
        return currentRoom;
    }

    private Room getInputRoom(PartiallyUpdateInput input){
        Room inputRoom = Objects.requireNonNull(conversionService.convert(input, Room.RoomBuilder.class))
                .beds(input.getBedSizes() != null ?
                        input.getBedSizes().stream().map(bed ->
                                bedRepository.findBySize(BedSize.getByCode(bed)).orElseThrow()
                        ).toList() : null)
                .build();
        return inputRoom;
    }

    @SneakyThrows
    private Room mapRooms(Room currentRoom,Room inputRoom){
        JsonNode roomNode = mapper.valueToTree(currentRoom);
        JsonNode inputNode = mapper.valueToTree(inputRoom);

        JsonMergePatch patch = JsonMergePatch.fromJson(inputNode);
        Room updatedRoom = mapper.treeToValue(patch.apply(roomNode), Room.class);

        return updatedRoom;
    }

    private void isRoomNumberUnique(Room currentRoom, Room updatedRoom){
        if (!Objects.equals(currentRoom.getRoomNumber(), updatedRoom.getRoomNumber()) && roomRepository.existsByRoomNumber(updatedRoom.getRoomNumber())) {
            throw new HotelApiException(
                    "Room number must be unique",
                    HttpStatus.BAD_REQUEST);
        }
    }

    public PartiallyUpdateOutput partiallyUpdate(PartiallyUpdateInput input) {
        logStart(input);

        validateInput(input);

        checkBedCount(input);

        checkBathroomType(input);

        Room currentRoom = getCurrentRoom(input);
        Room inputRoom = getInputRoom(input);

        Room updatedRoom = mapRooms(currentRoom, inputRoom);

        isRoomNumberUnique(currentRoom, updatedRoom);


        roomRepository.save(updatedRoom);

        PartiallyUpdateOutput output = conversionService.convert(updatedRoom, PartiallyUpdateOutput.class);
        logEnd(output);
        return output;
    }


}
