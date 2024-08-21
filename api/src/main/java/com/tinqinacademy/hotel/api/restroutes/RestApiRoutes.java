package com.tinqinacademy.hotel.api.restroutes;

public class RestApiRoutes {
    public final static String API = "/api/v1";


    public final static String API_HOTEL = API + "/hotel";
    public final static String API_HOTEL_GET_ROOM = API_HOTEL + "/{roomId}";
    public final static String API_HOTEL_CHECK_AVAILABILITY = API_HOTEL + "/rooms";
    public final static String API_HOTEL_BOOK_ROOM = API_HOTEL + "/{roomId}";
    public final static String API_HOTEL_UNBOOK_ROOM = API_HOTEL + "/{bookingId}";
    public final static String API_HOTEL_GET_ROOM_BY_ROOM_NUMBER = API_HOTEL + "/room/{roomNumber}";


    public final static String API_SYSTEM = API + "/system";
    public final static String API_SYSTEM_ADD_ROOM = API_SYSTEM + "/room";
    public final static String API_SYSTEM_REGISTER_VISITOR = API_SYSTEM + "/register";
    public final static String API_SYSTEM_VISITOR_REPORT = API_SYSTEM + "/register";
    public final static String API_SYSTEM_DELETE_ROOM = API_SYSTEM + "/room/{roomId}";
    public final static String API_SYSTEM_UPDATE_ROOM = API_SYSTEM + "/room/{roomId}";
    public final static String API_SYSTEM_UPDATE_PARTIALLY_ROOM = API_SYSTEM + "/room/{roomId}";
}
