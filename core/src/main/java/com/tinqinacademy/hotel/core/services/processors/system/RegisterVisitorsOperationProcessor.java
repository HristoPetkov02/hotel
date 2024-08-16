package com.tinqinacademy.hotel.core.services.processors.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.hotel.api.interfaces.ErrorHandlerService;
import com.tinqinacademy.hotel.api.model.ErrorWrapper;
import com.tinqinacademy.hotel.api.model.input.VisitorRegisterInput;
import com.tinqinacademy.hotel.api.operations.system.registervisitors.RegisterVisitorsInput;
import com.tinqinacademy.hotel.api.operations.system.registervisitors.RegisterVisitorsOperation;
import com.tinqinacademy.hotel.api.operations.system.registervisitors.RegisterVisitorsOutput;
import com.tinqinacademy.hotel.core.services.base.BaseOperationProcessor;
import com.tinqinacademy.hotel.api.exceptions.HotelApiException;
import com.tinqinacademy.hotel.persistence.models.Booking;
import com.tinqinacademy.hotel.persistence.models.Guest;
import com.tinqinacademy.hotel.persistence.models.Room;
import com.tinqinacademy.hotel.persistence.repository.BookingRepository;
import com.tinqinacademy.hotel.persistence.repository.GuestRepository;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RegisterVisitorsOperationProcessor extends BaseOperationProcessor<RegisterVisitorsInput, RegisterVisitorsOutput> implements RegisterVisitorsOperation {
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final GuestRepository guestRepository;

    public RegisterVisitorsOperationProcessor(ConversionService conversionService, ObjectMapper mapper, ErrorHandlerService errorHandlerService, Validator validator, RoomRepository roomRepository, BookingRepository bookingRepository, GuestRepository guestRepository) {
        super(conversionService, mapper, errorHandlerService, validator);
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.guestRepository = guestRepository;
    }

    @Override
    public Either<ErrorWrapper, RegisterVisitorsOutput> process(RegisterVisitorsInput input) {
        return Try.of(() -> registerVisitors(input))
                .toEither()
                .mapLeft(errorHandlerService::handle);
    }


    private Booking getCurrentBooking(VisitorRegisterInput visitorRegister) {
        Room room = roomRepository.findRoomByRoomNumber(visitorRegister.getRoomNo())
                .orElseThrow(() -> new HotelApiException(
                        String.format("Room with number %s not found", visitorRegister.getRoomNo()),
                        HttpStatus.NOT_FOUND));

        Booking currentBooking = bookingRepository.findBookingByRoomAndStartDateAndEndDate(room, visitorRegister.getStartDate(), visitorRegister.getEndDate())
                .orElseThrow(() -> new HotelApiException(
                        String.format("Booking with room number %s, start date %s and end date %s not found", visitorRegister.getRoomNo(), visitorRegister.getStartDate(), visitorRegister.getEndDate()),
                        HttpStatus.NOT_FOUND
                ));
        return currentBooking;
    }


    private void registerVisitor(VisitorRegisterInput visitorRegister) {
        Booking booking = getCurrentBooking(visitorRegister);

        Guest guest = conversionService.convert(visitorRegister, Guest.class);
        if (guest == null)
            throw new HotelApiException(
                    "Error converting VisitorRegisterInput to Guest",
                    HttpStatus.BAD_REQUEST
            );

        Guest newGuest = guestRepository.save(guest);

        booking.getGuests().add(newGuest);

        bookingRepository.save(booking);
    }


    public RegisterVisitorsOutput registerVisitors(RegisterVisitorsInput input) {
        logStart(input);
        validateInput(input);

        for (VisitorRegisterInput visitorRegister : input.getVisitorRegisterInputs()) {
            registerVisitor(visitorRegister);
        }

        RegisterVisitorsOutput output = RegisterVisitorsOutput
                .builder()
                .build();

        logEnd(output);
        return output;
    }

}
