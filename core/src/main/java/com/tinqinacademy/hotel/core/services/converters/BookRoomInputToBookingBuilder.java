package com.tinqinacademy.hotel.core.services.converters;

import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomInput;
import com.tinqinacademy.hotel.core.services.base.BaseConverter;
import com.tinqinacademy.hotel.persistence.models.Booking;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class BookRoomInputToBookingBuilder extends BaseConverter<BookRoomInput, Booking.BookingBuilder> {

    @Override
    public Booking.BookingBuilder convertObject(BookRoomInput input) {
        Booking.BookingBuilder output = Booking.builder()
                .userId(UUID.fromString(input.getUserId()))
                .startDate(input.getStartDate())
                .endDate(input.getEndDate());
        return output;
    }
}
