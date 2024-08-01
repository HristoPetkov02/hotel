package com.tinqinacademy.hotel.core.services.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.hotel.api.interfaces.ErrorHandlerService;
import com.tinqinacademy.hotel.api.model.ErrorWrapper;
import com.tinqinacademy.hotel.api.operations.removebookedroom.RemoveBookedRoomInput;
import com.tinqinacademy.hotel.api.operations.removebookedroom.RemoveBookedRoomOperation;
import com.tinqinacademy.hotel.api.operations.removebookedroom.RemoveBookedRoomOutput;
import com.tinqinacademy.hotel.core.services.base.BaseOperationProcessor;
import com.tinqinacademy.hotel.core.services.exceptions.HotelApiException;
import com.tinqinacademy.hotel.persistence.models.Booking;
import com.tinqinacademy.hotel.persistence.repository.BookingRepository;
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
public class RemoveBookedRoomOperationProcessor extends BaseOperationProcessor<RemoveBookedRoomInput, RemoveBookedRoomOutput> implements RemoveBookedRoomOperation {
    private final BookingRepository bookingRepository;

    public RemoveBookedRoomOperationProcessor(ConversionService conversionService, ObjectMapper mapper, ErrorHandlerService errorHandlerService, Validator validator, BookingRepository bookingRepository) {
        super(conversionService, mapper, errorHandlerService, validator);
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Either<ErrorWrapper, RemoveBookedRoomOutput> process(RemoveBookedRoomInput input) {
        return Try.of(() -> unbookRoom(input))
                .toEither()
                .mapLeft(errorHandlerService::handle);
    }

    private void checkBookingExists(RemoveBookedRoomInput input) {
        bookingRepository.findById(UUID.fromString(input.getBookingId()))
                .orElseThrow(() -> new HotelApiException(
                        String.format("Booking with id %s not found", input.getBookingId()),
                        HttpStatus.NOT_FOUND));
    }

    private RemoveBookedRoomOutput unbookRoom(RemoveBookedRoomInput input) {
        logStart(input);

        checkBookingExists(input);


        bookingRepository.deleteById(UUID.fromString(input.getBookingId()));
        bookingRepository.deleteGuestsNotInBooking();

        RemoveBookedRoomOutput output = RemoveBookedRoomOutput
                .builder()
                .build();

        logEnd(output);
        return output;
    }
}
