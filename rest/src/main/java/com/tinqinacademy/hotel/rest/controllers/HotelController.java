package com.tinqinacademy.hotel.rest.controllers;



import com.tinqinacademy.hotel.api.operations.hotel.availablerooms.AvailableRoomsInput;
import com.tinqinacademy.hotel.api.operations.hotel.availablerooms.AvailableRoomsOperation;
import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomOperation;
import com.tinqinacademy.hotel.api.operations.hotel.getbyroomnumber.GetByRoomNumberInput;
import com.tinqinacademy.hotel.api.operations.hotel.getbyroomnumber.GetByRoomNumberOperation;
import com.tinqinacademy.hotel.api.operations.hotel.getroom.GetRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.getroom.GetRoomOperation;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomOperation;
import com.tinqinacademy.hotel.api.restroutes.RestApiRoutes;
import com.tinqinacademy.hotel.rest.base.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@RequiredArgsConstructor
public class HotelController extends BaseController {
    private final GetRoomOperation getRoomOperation;
    private final BookRoomOperation bookRoomOperation;
    private final AvailableRoomsOperation availableRoomsOperation;
    private final UnbookRoomOperation unbookRoomOperation;
    private final GetByRoomNumberOperation getByRoomNumberOperation;


    @Operation(summary = "Search room by roomId", description = " This endpoint is for searching a room by roomId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the room"),
            @ApiResponse(responseCode = "400", description = "Wrong roomId format used"),
            @ApiResponse(responseCode = "404", description = "A room with this roomId doesn't exist")
    })
    @GetMapping(RestApiRoutes.API_HOTEL_GET_ROOM)
    public ResponseEntity<?> getRoom(@PathVariable String roomId) {

        GetRoomInput input = GetRoomInput
                .builder()
                .roomId(roomId)
                .build();

        return handle(getRoomOperation.process(input));
    }


    @Operation(summary = "Check available rooms", description = " This endpoint is for checking available rooms by criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully have been displayed the ids for the available rooms "),
            @ApiResponse(responseCode = "400", description = "The criteria has a wrong input"),
            @ApiResponse(responseCode = "404", description = "There are no available rooms with the required search criteria")
    })
    @GetMapping(RestApiRoutes.API_HOTEL_CHECK_AVAILABILITY)
    public ResponseEntity<?> checkAvailability(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(value = "bedCount", required = false) Integer bedCount,
            @RequestParam(value = "bedSize", required = false) String bedSize,
            @RequestParam(value = "bathroomType", required = false) String bathroomType) {
        AvailableRoomsInput input = AvailableRoomsInput
                .builder()
                .startDate(startDate)
                .endDate(endDate)
                .bedCount(bedCount)
                .bedSize(bedSize)
                .bathroomType(bathroomType)
                .build();

        return handle(availableRoomsOperation.process(input));
    }

    @Operation(summary = "Book a room", description = " This endpoint is booking a room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully has been booked the room "),
            @ApiResponse(responseCode = "400", description = "The room is unavailable"),
            @ApiResponse(responseCode = "404", description = "The room doesn't exist")
    })
    @PostMapping(RestApiRoutes.API_HOTEL_BOOK_ROOM)
    public ResponseEntity<?> bookRoom(@PathVariable String roomId, @RequestBody BookRoomInput input) {
        BookRoomInput updatedInput = input
                .toBuilder()
                .roomId(roomId)
                .build();

        return handle(bookRoomOperation.process(updatedInput));
    }


    @Operation(summary = "Remove booked room", description = " This endpoint is removing the booked status of a room making it available using the bookingId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The room was successfully unbooked"),
            @ApiResponse(responseCode = "400", description = "The bookingId is in the wrong format"),
            @ApiResponse(responseCode = "404", description = "There is no booked room with this bookingId")
    })
    @DeleteMapping(RestApiRoutes.API_HOTEL_UNBOOK_ROOM)
    public ResponseEntity<?> unbookRoom(@PathVariable String bookingId,@RequestBody UnbookRoomInput input) {
        UnbookRoomInput updatedInput = input.toBuilder()
                .bookingId(bookingId)
                .build();

        return handle(unbookRoomOperation.process(updatedInput));
    }

    @Operation(summary = "Search room by roomNumber", description = " This endpoint is for searching a room by roomNumber")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the room"),
            @ApiResponse(responseCode = "400", description = "Wrong roomNumber format used"),
            @ApiResponse(responseCode = "404", description = "A room with this roomNumber doesn't exist")
    })
    @GetMapping(RestApiRoutes.API_HOTEL_GET_ROOM_BY_ROOM_NUMBER)
    public ResponseEntity<?> getRoomByRoomNumber(@PathVariable String roomNumber) {
        GetByRoomNumberInput input = GetByRoomNumberInput.builder()
                .roomNumber(roomNumber)
                .build();
        return handle(getByRoomNumberOperation.process(input));
    }



}
