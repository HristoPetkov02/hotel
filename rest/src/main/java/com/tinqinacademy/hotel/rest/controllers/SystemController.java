package com.tinqinacademy.hotel.rest.controllers;


import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomInput;
import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomOperation;
import com.tinqinacademy.hotel.api.operations.system.deleteroom.DeleteRoomInput;
import com.tinqinacademy.hotel.api.operations.system.deleteroom.DeleteRoomOperation;
import com.tinqinacademy.hotel.api.operations.system.partiallyupdate.PartiallyUpdateInput;
import com.tinqinacademy.hotel.api.operations.system.partiallyupdate.PartiallyUpdateOperation;
import com.tinqinacademy.hotel.api.operations.system.registervisitors.RegisterVisitorsInput;
import com.tinqinacademy.hotel.api.operations.system.registervisitors.RegisterVisitorsOperation;
import com.tinqinacademy.hotel.api.operations.system.report.ReportInput;
import com.tinqinacademy.hotel.api.operations.system.report.ReportOperation;
import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomInput;
import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomOperation;

import com.tinqinacademy.hotel.api.restroutes.RestApiRoutes;
import com.tinqinacademy.hotel.rest.base.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@RequiredArgsConstructor
public class SystemController extends BaseController {
    private final AddRoomOperation addRoomOperation;
    private final DeleteRoomOperation deleteRoomOperation;
    private final UpdateRoomOperation updateRoomOperation;
    private final RegisterVisitorsOperation registerVisitorsOperation;
    private final PartiallyUpdateOperation partiallyUpdateOperation;
    private final ReportOperation reportOperation;


    @Operation(summary = "Adds a room", description = " This endpoint is for adding a room to the hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The room was added/created"),
            @ApiResponse(responseCode = "400", description = "The room already exists")
    })
    @PostMapping(RestApiRoutes.API_SYSTEM_ADD_ROOM)
    public ResponseEntity<?> addRoom(@RequestBody AddRoomInput input) {

        return handleWithCode(addRoomOperation.process(input),HttpStatus.CREATED);
    }



    @Operation(summary = "Register visitors", description = " This endpoint is registering a list of visitors as a room renters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The visitors have been registered"),
            @ApiResponse(responseCode = "400", description = "Incorrect data format"),
            @ApiResponse(responseCode = "404", description = "The booking was not found")
    })
    @PostMapping(RestApiRoutes.API_SYSTEM_REGISTER_VISITOR)
    public ResponseEntity<?> registerVisitors(@RequestBody RegisterVisitorsInput input) {
        return handleWithCode(registerVisitorsOperation.process(input),HttpStatus.CREATED);
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
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String phoneNo,
            @RequestParam(required = false) String idCardNo,
            @RequestParam(required = false) LocalDate idCardValidity,
            @RequestParam(required = false) String idCardIssueAthority,
            @RequestParam(required = false) LocalDate idCardIssueDate,
            @RequestParam(required = false) String roomNo) {
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

        return handle(reportOperation.process(input));
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

        return handle(deleteRoomOperation.process(input));
    }




    @Operation(summary = "Update room", description = " This endpoint is for updating the info of a room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The room was successfully updated"),
            @ApiResponse(responseCode = "400", description = "The roomId is in the wrong format"),
            @ApiResponse(responseCode = "404", description = "There is no room with this id")
    })
    @PutMapping(RestApiRoutes.API_SYSTEM_UPDATE_ROOM)
    public ResponseEntity<?> updateRoom(@PathVariable String roomId, @RequestBody UpdateRoomInput input) {
        UpdateRoomInput updatedInput = input.toBuilder()
                .roomId(roomId)
                .build();
        return handle(updateRoomOperation.process(updatedInput));
    }


    @Operation(summary = "Update partially room", description = " This endpoint is for updating partially the info of a room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The room was successfully updated"),
            @ApiResponse(responseCode = "400", description = "The roomId is in the wrong format"),
            @ApiResponse(responseCode = "404", description = "There is no room with this id")
    })
    @PatchMapping(value = RestApiRoutes.API_SYSTEM_UPDATE_PARTIALLY_ROOM, consumes = "application/json-patch+json")
    public ResponseEntity<?> partiallyUpdate(@PathVariable String roomId, @RequestBody PartiallyUpdateInput input) {
        PartiallyUpdateInput updatedInput = input.toBuilder()
                .roomId(roomId)
                .build();
        return handle(partiallyUpdateOperation.process(updatedInput));
    }
}
