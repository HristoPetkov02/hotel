package com.tinqinacademy.hotel.restexport.clients;

import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomInput;
import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomOutput;
import com.tinqinacademy.hotel.api.operations.hotel.availablerooms.AvailableRoomsOutput;
import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomOutput;
import com.tinqinacademy.hotel.api.operations.hotel.getroom.GetRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.partiallyupdate.PartiallyUpdateInput;
import com.tinqinacademy.hotel.api.operations.system.partiallyupdate.PartiallyUpdateOutput;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomInput;
import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomOutput;
import com.tinqinacademy.hotel.api.restroutes.RestApiRoutes;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;


public interface HotelRestExport {
    //hotel rest export
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

    @RequestLine("POST " + RestApiRoutes.API_HOTEL_BOOK_ROOM)
    @Headers("Content-Type: application/json")
    BookRoomOutput bookRoom(@Param("roomId") String roomId, @RequestBody BookRoomInput input);

    @RequestLine("DELETE " + RestApiRoutes.API_HOTEL_UNBOOK_ROOM)
    @Headers("Content-Type: application/json")
    UnbookRoomOutput unbookRoom(@Param("bookingId") String bookingId, @RequestBody UnbookRoomInput input);




    //system rest export
    @RequestLine("PUT " + RestApiRoutes.API_SYSTEM_UPDATE_ROOM)
    @Headers("Content-Type: application/json")
    UpdateRoomOutput updateRoom(@Param("roomId") String roomId, @RequestBody UpdateRoomInput input);

    @RequestLine("PATCH " + RestApiRoutes.API_SYSTEM_UPDATE_PARTIALLY_ROOM)
    @Headers("Content-Type: application/json-patch+json")
    PartiallyUpdateOutput partiallyUpdate(@Param("roomId") String roomId,@RequestBody PartiallyUpdateInput input);



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


    @




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
