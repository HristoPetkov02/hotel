package com.tinqinacademy.hotel.restexport.clients;

import com.tinqinacademy.hotel.api.operations.addroom.AddRoomInput;
import com.tinqinacademy.hotel.api.operations.addroom.AddRoomOutput;
import com.tinqinacademy.hotel.api.operations.availablerooms.AvailableRoomsOutput;
import com.tinqinacademy.hotel.api.operations.getroom.GetRoomOutput;
import com.tinqinacademy.hotel.api.operations.partiallyupdate.PartiallyUpdateInput;
import com.tinqinacademy.hotel.api.operations.partiallyupdate.PartiallyUpdateOutput;
import com.tinqinacademy.hotel.api.operations.updateroom.UpdateRoomInput;
import com.tinqinacademy.hotel.api.operations.updateroom.UpdateRoomOutput;
import com.tinqinacademy.hotel.api.restroutes.RestApiRoutes;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;


public interface HotelRestExport {
    @RequestLine("GET " + RestApiRoutes.API_HOTEL_GET_ROOM)
    @Headers("Content-Type: application/json")
    GetRoomOutput getRoom(@Param("roomId") String roomId);

    @RequestLine("GET " + RestApiRoutes.API_HOTEL_CHECK_AVAILABILITY + "?startDate={startDate}&endDate={endDate}&bedCount={bedCount}&bedSize={bedSize}&bathroomType={bathroomType}")
    @Headers("Content-Type: application/json")
    AvailableRoomsOutput checkAvailability(@Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate,
                                           @Param("bedCount") Integer bedCount,
                                           @Param("bedSize") String bedSize,
                                           @Param("bathroomType") String bathroomType);

    @RequestLine("PUT " + RestApiRoutes.API_SYSTEM_UPDATE_ROOM)
    @Headers("Content-Type: application/json")
    UpdateRoomOutput updateRoom(@Param("roomId") String roomId, @RequestBody UpdateRoomInput input);

    @RequestLine("PATCH " + RestApiRoutes.API_SYSTEM_UPDATE_PARTIALLY_ROOM)
    @Headers("Content-Type: application/json-patch+json")
    PartiallyUpdateOutput partiallyUpdate(@Param("roomId") String roomId, PartiallyUpdateInput input);


    //system rest export
    @RequestLine("POST " + RestApiRoutes.API_SYSTEM_ADD_ROOM)
    @Headers("Content-Type: application/json")
    AddRoomOutput addRoom(@RequestBody AddRoomInput input);
    /*
    @GetMapping(RestApiRoutes.API_HOTEL_CHECK_AVAILABILITY)
    ResponseEntity<AvailableRoomsOutput> checkAvailability(@RequestParam LocalDate startDate,
                                           @RequestParam LocalDate endDate,
                                           @RequestParam(value = "bedCount", required = false) Integer bedCount,
                                           @RequestParam(value = "bedSize", required = false) String bedSize,
                                           @RequestParam(value = "bathroomType", required = false) String bathroomType);

    @PostMapping(RestApiRoutes.API_HOTEL_BOOK_ROOM)
    ResponseEntity<BookRoomOutput> bookRoom(@RequestParam String roomId, @RequestBody BookRoomInput input);

    @DeleteMapping(RestApiRoutes.API_HOTEL_UNBOOK_ROOM)
    ResponseEntity<RemoveBookedRoomOutput> unbookRoom(@PathVariable String bookingId);




    @PostMapping(RestApiRoutes.API_SYSTEM_REGISTER_VISITOR)
    ResponseEntity<RegisterVisitorsOutput> registerVisitors(@RequestBody RegisterVisitorsInput input);

    @GetMapping(RestApiRoutes.API_SYSTEM_VISITOR_REPORT)
    ResponseEntity<ReportOutput> reportByCriteria( @RequestParam LocalDate startDate,
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
    ResponseEntity<DeleteRoomOutput> deleteRoom(@PathVariable("roomId") String id);*/
}
