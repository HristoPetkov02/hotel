package com.tinqinacademy.hotel.core.services.services;


import com.tinqinacademy.hotel.api.model.BathroomType;
import com.tinqinacademy.hotel.api.model.BedSize;
import com.tinqinacademy.hotel.api.operations.availablerooms.AvailableRoomsInput;
import com.tinqinacademy.hotel.api.operations.availablerooms.AvailableRoomsOutput;
import com.tinqinacademy.hotel.api.operations.bookroom.BookRoomInput;
import com.tinqinacademy.hotel.api.operations.bookroom.BookRoomOutput;
import com.tinqinacademy.hotel.api.operations.getroom.GetRoomInput;
import com.tinqinacademy.hotel.api.operations.getroom.GetRoomOutput;
import com.tinqinacademy.hotel.api.operations.removebookedroom.RemoveBookedRoomInput;
import com.tinqinacademy.hotel.api.operations.removebookedroom.RemoveBookedRoomOutput;
import com.tinqinacademy.hotel.api.interfaces.HotelService;


import com.tinqinacademy.hotel.core.services.exceptions.HotelApiException;
import com.tinqinacademy.hotel.persistence.models.*;
import com.tinqinacademy.hotel.persistence.repository.GuestRepository;
import com.tinqinacademy.hotel.persistence.repository.UserRepository;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import com.tinqinacademy.hotel.persistence.repository.BookingRepository;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final ConversionService conversionService;
    private final UserRepository userRepository;
    private final GuestRepository guestRepository;







    @Override
    public AvailableRoomsOutput checkAvailableRooms(AvailableRoomsInput input) {
        log.info("Start checkAvailableRooms input = {}", input);

        List<Room> rooms = roomRepository.findAvailableRooms(input.getStartDate(), input.getEndDate()).stream()
                .filter(room -> input.getBedCount().map(count -> room.getBeds().size() == count).orElse(true))
                .filter(room -> input.getBedSize().map(size -> room.getBeds().stream().anyMatch(bed -> bed.getBedSize().toString().equals(size))).orElse(true))
                .filter(room -> input.getBathroomType().map(type -> room.getBathroomType().toString().equals(type)).orElse(true))
                .toList();

        if (rooms.isEmpty())
            throw new HotelApiException("No available rooms");

        AvailableRoomsOutput output = conversionService.convert(rooms, AvailableRoomsOutput.class);
        log.info("End checkAvailableRooms output = {}", output);
        return output;
    }

    @Override
    public RemoveBookedRoomOutput unbookRoom(RemoveBookedRoomInput input) {
        log.info("Start removeBookedRoom input = {}", input);

        Booking booking = bookingRepository.findById(UUID.fromString(input.getBookingId()))
                .orElseThrow(() -> new HotelApiException("No booking with this id"));


        bookingRepository.deleteById(UUID.fromString(input.getBookingId()));
        bookingRepository.deleteGuestsNotInBooking();

        RemoveBookedRoomOutput output = RemoveBookedRoomOutput
                .builder()
                .build();
        log.info("End removeBookedRoom output = {}", output);
        return output;
    }


}
