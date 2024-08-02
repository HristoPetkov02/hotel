package com.tinqinacademy.hotel.core.services.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.hotel.api.interfaces.ErrorHandlerService;
import com.tinqinacademy.hotel.api.model.ErrorWrapper;
import com.tinqinacademy.hotel.api.operations.deleteroom.DeleteRoomInput;
import com.tinqinacademy.hotel.api.operations.deleteroom.DeleteRoomOperation;
import com.tinqinacademy.hotel.api.operations.deleteroom.DeleteRoomOutput;
import com.tinqinacademy.hotel.core.services.base.BaseOperationProcessor;
import com.tinqinacademy.hotel.core.services.exceptions.HotelApiException;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service

public class DeleteRoomOperationProcessor extends BaseOperationProcessor<DeleteRoomInput,DeleteRoomOutput> implements DeleteRoomOperation {
    private final ErrorHandlerService errorHandlerService;
    private final RoomRepository roomRepository;

    public DeleteRoomOperationProcessor(ConversionService conversionService, ObjectMapper mapper, ErrorHandlerService errorHandlerService, Validator validator, ErrorHandlerService errorHandlerService1, RoomRepository roomRepository) {
        super(conversionService, mapper, errorHandlerService, validator);
        this.errorHandlerService = errorHandlerService1;
        this.roomRepository = roomRepository;
    }


    @Override
    public Either<ErrorWrapper, DeleteRoomOutput> process(DeleteRoomInput input) {
        return Try.of(() -> deleteRoom(input))
                .toEither()
                .mapLeft(errorHandlerService::handle);
    }


    private void checkRoomExists(DeleteRoomInput input) {
        if (!roomRepository.existsById(UUID.fromString(input.getId()))) {
            throw new HotelApiException(
                    String.format("Room with id %s not found", input.getId()),
                    HttpStatus.NOT_FOUND);
        }
    }


    private DeleteRoomOutput deleteRoom(DeleteRoomInput input) {
        logStart(input);

        validateInput(input);
        checkRoomExists(input);

        roomRepository.deleteById(UUID.fromString(input.getId()));
        DeleteRoomOutput output = DeleteRoomOutput.builder().build();
        logEnd(output);
        return output;
    }
}
