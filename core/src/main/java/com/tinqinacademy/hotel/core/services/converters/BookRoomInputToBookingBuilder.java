package com.tinqinacademy.hotel.core.services.converters;

import com.tinqinacademy.hotel.api.operations.bookroom.BookRoomInput;
import com.tinqinacademy.hotel.persistence.models.Booking;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BookRoomInputToBookingBuilder implements Converter<BookRoomInput, Booking.BookingBuilder> {

    @Override
    public Booking.BookingBuilder convert(BookRoomInput input) {
        log.info("Start converter BookRoomInputToBookingBuilder input = {}",input);
        Booking.BookingBuilder output = Booking.builder()
                .startDate(input.getStartDate())
                .endDate(input.getEndDate());
        log.info("End converter BookRoomInputToBookingBuilder output = {}",output);
        return output;
    }
}
