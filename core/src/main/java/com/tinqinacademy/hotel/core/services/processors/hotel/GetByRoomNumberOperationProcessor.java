package com.tinqinacademy.hotel.core.services.processors.hotel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.hotel.api.exceptions.HotelApiException;
import com.tinqinacademy.hotel.api.interfaces.ErrorHandlerService;
import com.tinqinacademy.hotel.api.model.ErrorWrapper;
import com.tinqinacademy.hotel.api.operations.hotel.getbyroomnumber.GetByRoomNumberInput;
import com.tinqinacademy.hotel.api.operations.hotel.getbyroomnumber.GetByRoomNumberOperation;
import com.tinqinacademy.hotel.api.operations.hotel.getbyroomnumber.GetByRoomNumberOutput;
import com.tinqinacademy.hotel.core.services.base.BaseOperationProcessor;
import com.tinqinacademy.hotel.persistence.models.Room;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class GetByRoomNumberOperationProcessor extends BaseOperationProcessor<GetByRoomNumberInput, GetByRoomNumberOutput> implements GetByRoomNumberOperation {
    private final RoomRepository roomRepository;

    public GetByRoomNumberOperationProcessor(ConversionService conversionService, ObjectMapper mapper, ErrorHandlerService errorHandlerService, Validator validator, RoomRepository roomRepository) {
        super(conversionService, mapper, errorHandlerService, validator);
        this.roomRepository = roomRepository;
    }

    @Override
    public Either<ErrorWrapper, GetByRoomNumberOutput> process(GetByRoomNumberInput input) {
        return Try.of(() -> getRoomByRoomNumber(input))
                .toEither()
                .mapLeft(errorHandlerService::handle);
    }

    private GetByRoomNumberOutput getRoomByRoomNumber(GetByRoomNumberInput input) {
        logStart(input);
        validateInput(input);

        Room  room = roomRepository.findRoomByRoomNumber(input.getRoomNumber())
                .orElseThrow(() -> new HotelApiException(
                        String.format("Room with %s room number does not exist", input.getRoomNumber()),
                        HttpStatus.NOT_FOUND));
        GetByRoomNumberOutput output = conversionService.convert(room, GetByRoomNumberOutput.class);
        logEnd(output);
        return output;
    }
}
