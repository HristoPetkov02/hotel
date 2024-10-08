package com.tinqinacademy.hotel.restexport.clients;

import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomInput;
import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomOutput;
import com.tinqinacademy.hotel.api.operations.hotel.availablerooms.AvailableRoomsOutput;
import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.deleteroom.DeleteRoomOutput;
import com.tinqinacademy.hotel.api.operations.hotel.getroom.GetRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.partiallyupdate.PartiallyUpdateInput;
import com.tinqinacademy.hotel.api.operations.system.partiallyupdate.PartiallyUpdateOutput;
import com.tinqinacademy.hotel.api.operations.system.registervisitors.RegisterVisitorsInput;
import com.tinqinacademy.hotel.api.operations.system.registervisitors.RegisterVisitorsOutput;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.report.ReportOutput;
import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomInput;
import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomOutput;
import com.tinqinacademy.hotel.api.restroutes.RestApiRoutes;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@FeignClient(name = "hotel-rest-export")
public interface HotelRestExportFeignClient {
    @GetMapping(RestApiRoutes.API_HOTEL_GET_ROOM)
    GetRoomOutput getRoom(@PathVariable String roomId);

    @GetMapping(RestApiRoutes.API_HOTEL_CHECK_AVAILABILITY)
    ResponseEntity<AvailableRoomsOutput> checkAvailability(@RequestParam LocalDate startDate,
                                                           @RequestParam LocalDate endDate,
                                                           @RequestParam(value = "bedCount", required = false) Integer bedCount,
                                                           @RequestParam(value = "bedSize", required = false) String bedSize,
                                                           @RequestParam(value = "bathroomType", required = false) String bathroomType);

    @PostMapping(RestApiRoutes.API_HOTEL_BOOK_ROOM)
    BookRoomOutput bookRoom(@RequestParam String roomId, @RequestBody BookRoomInput input);

    @DeleteMapping(RestApiRoutes.API_HOTEL_UNBOOK_ROOM)
    UnbookRoomOutput unbookRoom(@PathVariable String bookingId);


    //system rest export
    @PostMapping(RestApiRoutes.API_SYSTEM_ADD_ROOM)
    AddRoomOutput addRoom(@RequestBody AddRoomInput input);

    @PostMapping(RestApiRoutes.API_SYSTEM_REGISTER_VISITOR)
    RegisterVisitorsOutput registerVisitors(@RequestBody RegisterVisitorsInput input);

    @GetMapping(RestApiRoutes.API_SYSTEM_VISITOR_REPORT)
    ReportOutput reportByCriteria(@RequestParam LocalDate startDate,
                                                  @RequestParam LocalDate endDate,
                                                  @RequestParam(required = false) String firstName,
                                                  @RequestParam(required = false) String lastName,
                                                  @RequestParam(required = false) String phoneNo,
                                                  @RequestParam(required = false) String idCardNo,
                                                  @RequestParam(required = false) LocalDate idCardValidity,
                                                  @RequestParam(required = false) String idCardIssueAthority,
                                                  @RequestParam(required = false) LocalDate idCardIssueDate,
                                                  @RequestParam(required = false) String roomNo);

    @DeleteMapping(RestApiRoutes.API_SYSTEM_DELETE_ROOM)
    DeleteRoomOutput deleteRoom(@PathVariable("roomId") String id);

    @PutMapping(RestApiRoutes.API_SYSTEM_UPDATE_ROOM)
    UpdateRoomOutput updateRoom(@PathVariable String roomId, @RequestBody UpdateRoomInput input);

    @PatchMapping(value = RestApiRoutes.API_SYSTEM_UPDATE_PARTIALLY_ROOM, consumes = "application/json-patch+json")
    PartiallyUpdateOutput partiallyUpdate(@PathVariable String roomId, @RequestBody PartiallyUpdateInput input);
}
