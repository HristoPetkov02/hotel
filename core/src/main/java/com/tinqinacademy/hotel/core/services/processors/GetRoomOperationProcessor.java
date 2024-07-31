package com.tinqinacademy.hotel.core.services.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.hotel.api.base.OperationInput;
import com.tinqinacademy.hotel.api.base.OperationOutput;
import com.tinqinacademy.hotel.api.interfaces.ErrorHandlerService;
import com.tinqinacademy.hotel.api.model.BedSize;
import com.tinqinacademy.hotel.api.model.ErrorWrapper;
import com.tinqinacademy.hotel.api.operations.getroom.GetRoomInput;
import com.tinqinacademy.hotel.api.operations.getroom.GetRoomOperation;
import com.tinqinacademy.hotel.api.operations.getroom.GetRoomOutput;
import com.tinqinacademy.hotel.core.services.base.BaseOperationProcessor;
import com.tinqinacademy.hotel.core.services.exceptions.HotelApiException;
import com.tinqinacademy.hotel.persistence.models.Bed;
import com.tinqinacademy.hotel.persistence.models.Booking;
import com.tinqinacademy.hotel.persistence.models.Room;
import com.tinqinacademy.hotel.persistence.repository.BookingRepository;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class GetRoomOperationProcessor extends BaseOperationProcessor<GetRoomInput, GetRoomOutput> implements GetRoomOperation {
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    public GetRoomOperationProcessor(ConversionService conversionService, ObjectMapper mapper, ErrorHandlerService errorHandlerService, Validator validator, RoomRepository roomRepository, BookingRepository bookingRepository) {
        super(conversionService, mapper, errorHandlerService, validator);
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Either<ErrorWrapper, GetRoomOutput> process(GetRoomInput input) {
        return Try.of(() -> getRoom(input))
                .toEither()
                .mapLeft(errorHandlerService::handle);
    }

    private Room getCurrentRoom(GetRoomInput input) {
        Room room = roomRepository.findById(UUID.fromString(input.getRoomId()))
                .orElseThrow(() -> new HotelApiException(
                        String.format("Room with id %s not found", input.getRoomId()),
                        HttpStatus.NOT_FOUND
                ));
        return room;
    }

    private List<Booking> getBookings(Room room) {
        return bookingRepository.findAllBookingsByRoomId(room.getId()).orElse(new ArrayList<>());
    }

    private List<LocalDate> getDatesOccupied(List<Booking> bookings) {
        List<LocalDate> dates = new ArrayList<>();
        for (Booking booking : bookings) {
            LocalDate startDate = booking.getStartDate();
            LocalDate endDate = booking.getEndDate();
            while (startDate.isBefore(endDate)) {
                dates.add(startDate);
                startDate = startDate.plusDays(1);
            }
            dates.add(endDate);
        }
        return dates;
    }

    private List<BedSize> getBeds(Room room) {
        List<BedSize> bedSizes = new ArrayList<>();
        for (Bed bed : room.getBeds()) {
            bedSizes.add(BedSize.getByCode(bed.getBedSize().toString()));
        }
        return bedSizes;
    }

    private GetRoomOutput getRoom(GetRoomInput input) {
        logStart(input);

        Room room = getCurrentRoom(input);

        List<Booking> bookings = getBookings(room);

        List<LocalDate> dates = getDatesOccupied(bookings);

        List<BedSize> bedSizes = getBeds(room);


        GetRoomOutput output = Objects.requireNonNull(conversionService.convert(room, GetRoomOutput.GetRoomOutputBuilder.class))
                .bedSizes(bedSizes)
                .bedCount(bedSizes.size())
                .datesOccupied(dates)
                .build();

        logEnd(output);
        return output;
    }


}
