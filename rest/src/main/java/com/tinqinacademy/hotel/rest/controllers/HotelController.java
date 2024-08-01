package com.tinqinacademy.hotel.rest.controllers;



import com.tinqinacademy.hotel.api.model.ErrorWrapper;
import com.tinqinacademy.hotel.api.operations.availablerooms.AvailableRoomsInput;
import com.tinqinacademy.hotel.api.operations.availablerooms.AvailableRoomsOutput;
import com.tinqinacademy.hotel.api.operations.bookroom.BookRoomInput;
import com.tinqinacademy.hotel.api.operations.bookroom.BookRoomOutput;
import com.tinqinacademy.hotel.api.operations.getroom.GetRoomInput;
import com.tinqinacademy.hotel.api.operations.getroom.GetRoomOutput;
import com.tinqinacademy.hotel.api.operations.removebookedroom.RemoveBookedRoomInput;
import com.tinqinacademy.hotel.api.operations.removebookedroom.RemoveBookedRoomOutput;
import com.tinqinacademy.hotel.api.restroutes.RestApiRoutes;
import com.tinqinacademy.hotel.core.services.processors.AvailableRoomsOperationProcessor;
import com.tinqinacademy.hotel.core.services.processors.BookRoomOperationProcessor;
import com.tinqinacademy.hotel.core.services.processors.GetRoomOperationProcessor;
import com.tinqinacademy.hotel.core.services.processors.RemoveBookedRoomOperationProcessor;
import com.tinqinacademy.hotel.rest.base.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.vavr.control.Either;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class HotelController extends BaseController {
    private final GetRoomOperationProcessor getRoomOperationProcessor;
    private final BookRoomOperationProcessor bookRoomOperationProcessor;
    private final AvailableRoomsOperationProcessor availableRoomsOperationProcessor;
    private final RemoveBookedRoomOperationProcessor removeBookedRoomOperationProcessor;


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

        Either<ErrorWrapper, GetRoomOutput> output = getRoomOperationProcessor.process(input);
        return handle(output);
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
            @RequestParam(value = "bedCount", required = false) Optional<Integer> bedCount,
            @RequestParam(value = "bedSize", required = false) Optional<String> bedSize,
            @RequestParam(value = "bathroomType", required = false) Optional<String> bathroomType) {
        AvailableRoomsInput input = AvailableRoomsInput
                .builder()
                .startDate(startDate)
                .endDate(endDate)
                .bedCount(bedCount)
                .bedSize(bedSize)
                .bathroomType(bathroomType)
                .build();

        Either<ErrorWrapper, AvailableRoomsOutput> output = availableRoomsOperationProcessor.process(input);
        return handle(output);
    }

    @Operation(summary = "Book a room", description = " This endpoint is booking a room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully has been booked the room "),
            @ApiResponse(responseCode = "400", description = "The room is unavailable"),
            @ApiResponse(responseCode = "404", description = "The room doesn't exist")
    })
    @PostMapping(RestApiRoutes.API_HOTEL_BOOK_ROOM)
    public ResponseEntity<?> bookRoom(@RequestParam String roomId, @Valid @RequestBody BookRoomInput input) {
        BookRoomInput updatedInput = input
                .toBuilder()
                .roomId(roomId)
                .build();
        Either<ErrorWrapper, BookRoomOutput> output = bookRoomOperationProcessor.process(updatedInput);
        return handle(output);
    }


    @Operation(summary = "Remove booked room", description = " This endpoint is removing the booked status of a room making it available using the bookingId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The room was successfully unbooked"),
            @ApiResponse(responseCode = "400", description = "The bookingId is in the wrong format"),
            @ApiResponse(responseCode = "404", description = "There is no booked room with this bookingId")
    })
    @DeleteMapping(RestApiRoutes.API_HOTEL_UNBOOK_ROOM)
    public ResponseEntity<?> unbookRoom(@PathVariable String bookingId) {
        RemoveBookedRoomInput input = RemoveBookedRoomInput
                .builder()
                .bookingId(bookingId)
                .build();
        Either<ErrorWrapper, RemoveBookedRoomOutput> output = removeBookedRoomOperationProcessor.process(input);
        return handle(output);
    }





}
