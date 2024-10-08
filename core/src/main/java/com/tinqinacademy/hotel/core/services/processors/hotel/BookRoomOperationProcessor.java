package com.tinqinacademy.hotel.core.services.processors.hotel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.hotel.api.interfaces.ErrorHandlerService;
import com.tinqinacademy.hotel.api.model.ErrorWrapper;
import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomOperation;
import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomOutput;
import com.tinqinacademy.hotel.core.services.base.BaseOperationProcessor;
import com.tinqinacademy.hotel.api.exceptions.HotelApiException;
import com.tinqinacademy.hotel.persistence.models.Booking;
import com.tinqinacademy.hotel.persistence.models.Room;
import com.tinqinacademy.hotel.persistence.repository.BookingRepository;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class BookRoomOperationProcessor extends BaseOperationProcessor<BookRoomInput, BookRoomOutput> implements BookRoomOperation {
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    public BookRoomOperationProcessor(ConversionService conversionService, ObjectMapper mapper, ErrorHandlerService errorHandlerService, Validator validator, RoomRepository roomRepository, BookingRepository bookingRepository) {
        super(conversionService, mapper, errorHandlerService, validator);
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Either<ErrorWrapper, BookRoomOutput> process(BookRoomInput input) {
        return Try.of(() -> bookRoom(input))
                .toEither()
                .mapLeft(errorHandlerService::handle);
    }

    private Room getCurrentRoom(BookRoomInput input) {
        Room room = roomRepository.findById(UUID.fromString(input.getRoomId()))
                .orElseThrow(() -> new HotelApiException(
                        String.format("Room with id %s not found", input.getRoomId()),
                        HttpStatus.NOT_FOUND
                ));
        return room;
    }

    private void checkIfRoomAvailability(BookRoomInput input, Room room) {
        List<Room> availableRooms = roomRepository.findAvailableRooms(input.getStartDate(), input.getEndDate());

        if (!availableRooms.contains(room)) {
            throw new HotelApiException(
                    String.format("Room with id %s is not available", room.getId()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private void checkIfStartDateIsBeforeEndDate(BookRoomInput input) {
        if (input.getStartDate().isAfter(input.getEndDate())) {
            throw new HotelApiException(
                    "Start date must be before end date",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public BookRoomOutput bookRoom(BookRoomInput input) {
        logStart(input);
        validateInput(input);
        checkIfStartDateIsBeforeEndDate(input);

        Room room = getCurrentRoom(input);

        checkIfRoomAvailability(input, room);

        BigDecimal totalPrice = room.getPrice().multiply(BigDecimal.valueOf(input.getStartDate().until(input.getEndDate()).getDays() + 1));

        Booking booking = Objects.requireNonNull(conversionService.convert(input, Booking.BookingBuilder.class))
                .totalPrice(totalPrice)
                .room(room)
                .build();

        bookingRepository.save(booking);

        BookRoomOutput output = BookRoomOutput
                .builder()
                .build();
        logEnd(output);
        return output;
    }
}
