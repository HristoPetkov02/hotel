package com.tinqinacademy.hotel.core.services.processors.hotel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.hotel.api.interfaces.ErrorHandlerService;
import com.tinqinacademy.hotel.api.model.ErrorWrapper;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomOperation;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomOutput;
import com.tinqinacademy.hotel.core.services.base.BaseOperationProcessor;
import com.tinqinacademy.hotel.api.exceptions.HotelApiException;
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
public class UnbookRoomOperationProcessor extends BaseOperationProcessor<UnbookRoomInput, UnbookRoomOutput> implements UnbookRoomOperation {
    private final BookingRepository bookingRepository;

    public UnbookRoomOperationProcessor(ConversionService conversionService, ObjectMapper mapper, ErrorHandlerService errorHandlerService, Validator validator, BookingRepository bookingRepository) {
        super(conversionService, mapper, errorHandlerService, validator);
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Either<ErrorWrapper, UnbookRoomOutput> process(UnbookRoomInput input) {
        return Try.of(() -> unbookRoom(input))
                .toEither()
                .mapLeft(errorHandlerService::handle);
    }

    private void checkIfUserIsOwner(UnbookRoomInput input) {
        Booking booking = bookingRepository.findById(UUID.fromString(input.getBookingId()))
                .orElseThrow(() -> new HotelApiException(
                        String.format("Booking with id %s not found", input.getBookingId()),
                        HttpStatus.NOT_FOUND));
        if (!booking.getUserId().equals(UUID.fromString(input.getUserId()))) {
            throw new HotelApiException("User is not the owner of the booking", HttpStatus.BAD_REQUEST);
        }
    }

    private void checkBookingExists(UnbookRoomInput input) {
        bookingRepository.findById(UUID.fromString(input.getBookingId()))
                .orElseThrow(() -> new HotelApiException(
                        String.format("Booking with id %s not found", input.getBookingId()),
                        HttpStatus.NOT_FOUND));
    }

    private UnbookRoomOutput unbookRoom(UnbookRoomInput input) {
        logStart(input);

        validateInput(input);

        checkBookingExists(input);
        checkIfUserIsOwner(input);


        bookingRepository.deleteById(UUID.fromString(input.getBookingId()));
        bookingRepository.deleteGuestsNotInBooking();

        UnbookRoomOutput output = UnbookRoomOutput
                .builder()
                .build();

        logEnd(output);
        return output;
    }
}
