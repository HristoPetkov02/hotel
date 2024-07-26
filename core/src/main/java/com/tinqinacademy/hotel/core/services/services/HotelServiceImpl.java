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
    public GetRoomOutput getRoom(GetRoomInput input) {
        log.info("Start getRoom input = {}", input);
        Room room = roomRepository.findById(UUID.fromString(input.getRoomId()))
                .orElseThrow(() -> new HotelApiException("No room with this id"));


        List<Booking> bookings = bookingRepository.findAllBookingsByRoomId(room.getId()).orElse(new ArrayList<>());

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

        List<BedSize> bedSizes = new ArrayList<>();

        for (Bed bed : room.getBeds()) {
            bedSizes.add(BedSize.getByCode(bed.getBedSize().toString()));
        }


        GetRoomOutput output = Objects.requireNonNull(conversionService.convert(room, GetRoomOutput.GetRoomOutputBuilder.class))
                .bedSizes(bedSizes)
                .bedCount(bedSizes.size())
                .datesOccupied(dates)
                .build();

        log.info("End getRoom output = {}", output);
        return output;
    }

    @Override
    public BookRoomOutput bookRoom(BookRoomInput input) {
        log.info("Start bookRoom input = {}", input);
        Room room = roomRepository.findById(UUID.fromString(input.getRoomId()))
                .orElseThrow(() -> new HotelApiException("No room with this id"));
        User user = userRepository.findUserByNameAndPhone(input.getFirstName(), input.getLastName(), input.getPhoneNo())
                .orElseThrow(() -> new HotelApiException("No such user"));

        List<Room> availableRooms = roomRepository.findAvailableRooms(input.getStartDate(), input.getEndDate());

        if (!availableRooms.contains(room)) {
            throw new HotelApiException("Room is not available");
        }

        // If I don't add 1 to the days, the calculation is wrong
        BigDecimal totalPrice = room.getPrice().multiply(BigDecimal.valueOf(input.getStartDate().until(input.getEndDate()).getDays() + 1));

        Booking booking = Objects.requireNonNull(conversionService.convert(input, Booking.BookingBuilder.class))
                .totalPrice(totalPrice)
                .room(room)
                .user(user)
                .build();

        bookingRepository.save(booking);

        BookRoomOutput output = BookRoomOutput
                .builder()
                .build();
        log.info("End bookRoom output = {}", output);
        return output;
    }

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

        RemoveBookedRoomOutput output = RemoveBookedRoomOutput
                .builder()
                .build();
        log.info("End removeBookedRoom output = {}", output);
        return output;
    }


}
