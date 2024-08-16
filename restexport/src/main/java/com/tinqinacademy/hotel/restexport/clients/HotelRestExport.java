package com.tinqinacademy.hotel.restexport.clients;

import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomInput;
import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomOutput;
import com.tinqinacademy.hotel.api.operations.hotel.availablerooms.AvailableRoomsOutput;
import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomOutput;
import com.tinqinacademy.hotel.api.operations.hotel.getroom.GetRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.deleteroom.DeleteRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.partiallyupdate.PartiallyUpdateInput;
import com.tinqinacademy.hotel.api.operations.system.partiallyupdate.PartiallyUpdateOutput;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.registervisitors.RegisterVisitorsInput;
import com.tinqinacademy.hotel.api.operations.system.registervisitors.RegisterVisitorsOutput;
import com.tinqinacademy.hotel.api.operations.system.report.ReportOutput;
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

    @RequestLine("GET " + RestApiRoutes.API_HOTEL_CHECK_AVAILABILITY +
            "?startDate={startDate}&endDate={endDate}&bedCount={bedCount}&bedSize={bedSize}&bathroomType={bathroomType}")
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
    @RequestLine("POST " + RestApiRoutes.API_SYSTEM_REGISTER_VISITOR)
    @Headers("Content-Type: application/json")
    RegisterVisitorsOutput registerVisitors(@RequestBody RegisterVisitorsInput input);

    @RequestLine("GET " + RestApiRoutes.API_SYSTEM_VISITOR_REPORT +
            "?startDate={startDate}&endDate={endDate}&firstName={firstName}&lastName={lastName}&phoneNo={phoneNo}&idCardNo={idCardNo}&idCardValidity={idCardValidity}&idCardIssueAthority={idCardIssueAthority}&idCardIssueDate={idCardIssueDate}&roomNo={roomNo}")
    @Headers("Content-Type: application/json")
    ReportOutput reportByCriteria(@Param("startDate") LocalDate startDate,
                                  @Param("endDate") LocalDate endDate,
                                  @Param("firstName") String firstName,
                                  @Param("lastName") String lastName,
                                  @Param("phoneNo") String phoneNo,
                                  @Param("idCardNo") String idCardNo,
                                  @Param("idCardValidity") LocalDate idCardValidity,
                                  @Param("idCardIssueAthority") String idCardIssueAthority,
                                  @Param("idCardIssueDate") LocalDate idCardIssueDate,
                                  @Param("roomNo") String roomNo);

    @RequestLine("POST " + RestApiRoutes.API_SYSTEM_ADD_ROOM)
    @Headers("Content-Type: application/json")
    AddRoomOutput addRoom(@RequestBody AddRoomInput input);


    @RequestLine("PUT " + RestApiRoutes.API_SYSTEM_UPDATE_ROOM)
    @Headers("Content-Type: application/json")
    UpdateRoomOutput updateRoom(@Param("roomId") String roomId, @RequestBody UpdateRoomInput input);

    @RequestLine("PATCH " + RestApiRoutes.API_SYSTEM_UPDATE_PARTIALLY_ROOM)
    @Headers("Content-Type: application/json-patch+json")
    PartiallyUpdateOutput partiallyUpdate(@Param("roomId") String roomId,@RequestBody PartiallyUpdateInput input);

    @RequestLine("DELETE " + RestApiRoutes.API_SYSTEM_DELETE_ROOM)
    @Headers("Content-Type: application/json")
    DeleteRoomOutput deleteRoom(@Param("roomId") String id);
}
