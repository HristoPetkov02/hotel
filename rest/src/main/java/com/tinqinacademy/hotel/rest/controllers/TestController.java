package com.tinqinacademy.hotel.rest.controllers;

import com.tinqinacademy.hotel.api.exceptions.HotelApiException;
import com.tinqinacademy.hotel.persistence.models.*;
import com.tinqinacademy.hotel.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    //this is a test controller to see if the repositories work as intended
    private final BedRepository bedRepository;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;



    @PostMapping("/bed/add")
    public ResponseEntity<?> addBed(@RequestBody Bed bed) {
        Bed result = bedRepository.save(bed);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/bed/{id}")
    public ResponseEntity<?> findByIdBed(@PathVariable UUID id) {
        Bed bed = bedRepository.findById(id).orElseThrow(()->new HotelApiException("Wrong id mate", HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(bed,HttpStatus.OK);
    }

    @GetMapping("/bed")
    public ResponseEntity<?> findAllBeds() {
        List<Bed> beds = bedRepository.findAll();
        return ResponseEntity.ok(beds);
    }

    @PutMapping("/bed/{id}")
    public ResponseEntity<?> updateBed(@PathVariable UUID id, @RequestBody Bed bed) {
        bed.setId(id);
        Bed updatedBed = bedRepository.save(bed);
        return ResponseEntity.ok(updatedBed);
    }

    @DeleteMapping("/bed/{id}")
    public ResponseEntity<Void> deleteByIdBed(@PathVariable UUID id) {
        bedRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/room/add")
    public ResponseEntity<?> add(@RequestBody Room room) {
        Room result = roomRepository.save(room);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<?> findByIdRoom(@PathVariable UUID id) {
        Room room = roomRepository.findById(id).orElseThrow(()->new HotelApiException("Wrong id mate",HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(room,HttpStatus.OK);
    }

    @GetMapping("/room")
    public ResponseEntity<?> findAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return ResponseEntity.ok(rooms);
    }

    @PutMapping("/room/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable UUID id, @RequestBody Room room) {
        room.setId(id);
        Room updatedRoom = roomRepository.save(room);
        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping("/room/{id}")
    public ResponseEntity<Void> deleteByIdRoom(@PathVariable UUID id) {
        roomRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }



    @PostMapping("/guest/add")
    public ResponseEntity<?> addGuest(@RequestBody Guest guest) {
        Guest result = guestRepository.save(guest);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/guest/{id}")
    public ResponseEntity<?> findByIdGuest(@PathVariable UUID id) {
        Guest guest = guestRepository.findById(id).orElseThrow(()->new HotelApiException("Wrong id mate",HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(guest,HttpStatus.OK);
    }

    @GetMapping("/guest")
    public ResponseEntity<?> findAllGuests() {
        List<Guest> guests = guestRepository.findAll();
        return ResponseEntity.ok(guests);
    }

    @PutMapping("/guest/{id}")
    public ResponseEntity<?> updateGuest(@PathVariable UUID id, @RequestBody Guest guest) {
        guest.setId(id);
        Guest updatedGuest = guestRepository.save(guest);
        return ResponseEntity.ok(updatedGuest);
    }

    @DeleteMapping("/guest/{id}")
    public ResponseEntity<Void> deleteByIdGuest(@PathVariable UUID id) {
        guestRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user")
    public ResponseEntity<?> findAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> findUserById(@PathVariable UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new HotelApiException("Wrong id mate",HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(user);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody User user) {
        user.setId(id);
        return ResponseEntity.ok(userRepository.save(user));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable UUID id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        return ResponseEntity.ok(userRepository.save(user));
    }

    @GetMapping("/booking")
    public ResponseEntity<?> findAllBookings() {
        return ResponseEntity.ok(bookingRepository.findAll());
    }

    @GetMapping("/booking/{id}")
    public ResponseEntity<?> findBookingById(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingRepository.findById(id).orElseThrow(() -> new HotelApiException("Wrong id mate",HttpStatus.NOT_FOUND)));
    }

    @PutMapping("/booking/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable UUID id, @RequestBody Booking booking) {
        booking.setId(id);
        return ResponseEntity.ok(bookingRepository.save(booking));
    }

    @DeleteMapping("/booking/{id}")
    public ResponseEntity<?> deleteBookingById(@PathVariable UUID id) {
        bookingRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/booking")
    public ResponseEntity<?> addBooking(@RequestBody Booking booking) {
        return ResponseEntity.ok(bookingRepository.save(booking));
    }

    @GetMapping("/booking/guest/{id}")
    public ResponseEntity<?> findBookingsByGuestId(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingRepository.findById(id));
    }


}
