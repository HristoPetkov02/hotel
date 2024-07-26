package com.tinqinacademy.hotel.rest.controllers;


import com.tinqinacademy.hotel.api.operations.addroom.AddRoomInput;
import com.tinqinacademy.hotel.api.operations.addroom.AddRoomOutput;
import com.tinqinacademy.hotel.api.operations.deleteroom.DeleteRoomInput;
import com.tinqinacademy.hotel.api.operations.deleteroom.DeleteRoomOutput;
import com.tinqinacademy.hotel.api.operations.partiallyupdate.PartiallyUpdateInput;
import com.tinqinacademy.hotel.api.operations.partiallyupdate.PartiallyUpdateOutput;
import com.tinqinacademy.hotel.api.operations.registervisitors.RegisterVisitorsInput;
import com.tinqinacademy.hotel.api.operations.registervisitors.RegisterVisitorsOutput;
import com.tinqinacademy.hotel.api.operations.report.ReportInput;
import com.tinqinacademy.hotel.api.operations.report.ReportOutput;
import com.tinqinacademy.hotel.api.operations.updateroom.UpdateRoomInput;
import com.tinqinacademy.hotel.api.operations.updateroom.UpdateRoomOutput;

import com.tinqinacademy.hotel.api.interfaces.SystemService;
import com.tinqinacademy.hotel.api.restroutes.RestApiRoutes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
public class SystemController {
    private final SystemService systemService;

    @Autowired
    public SystemController(SystemService systemService) {
        this.systemService = systemService;
    }

    @Operation(summary = "Adds a room", description = " This endpoint is for adding a room to the hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The room was added/created"),
            @ApiResponse(responseCode = "400", description = "The room already exists")
    })
    @PostMapping(RestApiRoutes.API_SYSTEM_ADD_ROOM)
    public ResponseEntity<?> addRoom(@Valid @RequestBody AddRoomInput input) {
        AddRoomOutput output = systemService.addRoom(input);
        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }


    @Operation(summary = "Register visitors", description = " This endpoint is registering a list of visitors as a room renters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The visitors have been registered"),
            @ApiResponse(responseCode = "400", description = "Incorrect data format")
    })
    @PostMapping(RestApiRoutes.API_SYSTEM_REGISTER_VISITOR)
    public ResponseEntity<?> registerVisitors(@Valid @RequestBody RegisterVisitorsInput input) {
        RegisterVisitorsOutput output = systemService.registerVisitors(input);
        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }


    @Operation(summary = "Report for visitors", description = " This endpoint is for displaying a report of visitors for a criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful report displayed"),
            @ApiResponse(responseCode = "400", description = "Incorrect data format")
    })
    @GetMapping(RestApiRoutes.API_SYSTEM_VISITOR_REPORT)
    public ResponseEntity<?> reportByCriteria(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String phoneNo,
            @RequestParam String idCardNo,
            @RequestParam LocalDate idCardValidity,
            @RequestParam String idCardIssueAthority,
            @RequestParam LocalDate idCardIssueDate,
            @RequestParam String roomNo) {
        ReportInput input = ReportInput
                .builder()
                .idCardIssueAthority(idCardIssueAthority)
                .idCardValidity(idCardValidity)
                .endDate(endDate)
                .firstName(firstName)
                .startDate(startDate)
                .idCardIssueDate(idCardIssueDate)
                .idCardNo(idCardNo)
                .roomNo(roomNo)
                .lastName(lastName)
                .phoneNo(phoneNo)
                .build();

        ReportOutput output = systemService.reportByCriteria(input);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @Operation(summary = "Remove a room", description = " This endpoint is removing a room from the hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The room was successfully removed"),
            @ApiResponse(responseCode = "400", description = "The roomId is in the wrong format"),
            @ApiResponse(responseCode = "404", description = "There is no room with this id")
    })
    @DeleteMapping(RestApiRoutes.API_SYSTEM_DELETE_ROOM)
    public ResponseEntity<?> deleteRoom(@PathVariable("roomId") String id) {
        DeleteRoomInput input = DeleteRoomInput.builder()
                .id(id)
                .build();
        DeleteRoomOutput output = systemService.deleteRoom(input);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @Operation(summary = "Update room", description = " This endpoint is for updating the info of a room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The room was successfully updated"),
            @ApiResponse(responseCode = "400", description = "The roomId is in the wrong format"),
            @ApiResponse(responseCode = "404", description = "There is no room with this id")
    })
    @PutMapping(RestApiRoutes.API_SYSTEM_UPDATE_ROOM)
    public ResponseEntity<?> updateRoom(@PathVariable String roomId, @Valid @RequestBody UpdateRoomInput input) {
        UpdateRoomInput updatedInput = input.toBuilder()
                .roomId(roomId)
                .build();
        UpdateRoomOutput output = systemService.updateRoom(updatedInput);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }


    @Operation(summary = "Update partially room", description = " This endpoint is for updating partially the info of a room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The room was successfully updated"),
            @ApiResponse(responseCode = "400", description = "The roomId is in the wrong format"),
            @ApiResponse(responseCode = "404", description = "There is no room with this id")
    })
    @PatchMapping(value = RestApiRoutes.API_SYSTEM_UPDATE_PARTIALLY_ROOM, consumes = "application/json-patch+json")
    public ResponseEntity<?> partiallyUpdate(@PathVariable String roomId,@Valid @RequestBody PartiallyUpdateInput input) {
        PartiallyUpdateInput updatedInput = input.toBuilder()
                .roomId(roomId)
                .build();
        PartiallyUpdateOutput output = systemService.partiallyUpdate(updatedInput);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }
}
