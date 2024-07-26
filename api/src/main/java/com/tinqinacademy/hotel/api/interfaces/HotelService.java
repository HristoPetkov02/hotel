package com.tinqinacademy.hotel.api.interfaces;


import com.tinqinacademy.hotel.api.operations.availablerooms.AvailableRoomsInput;
import com.tinqinacademy.hotel.api.operations.availablerooms.AvailableRoomsOutput;
import com.tinqinacademy.hotel.api.operations.bookroom.BookRoomInput;
import com.tinqinacademy.hotel.api.operations.bookroom.BookRoomOutput;
import com.tinqinacademy.hotel.api.operations.getroom.GetRoomInput;
import com.tinqinacademy.hotel.api.operations.getroom.GetRoomOutput;
import com.tinqinacademy.hotel.api.operations.removebookedroom.RemoveBookedRoomInput;
import com.tinqinacademy.hotel.api.operations.removebookedroom.RemoveBookedRoomOutput;

public interface HotelService {

    GetRoomOutput getRoom(GetRoomInput input);

    BookRoomOutput bookRoom(BookRoomInput input);

    AvailableRoomsOutput checkAvailableRooms(AvailableRoomsInput input);
    
    RemoveBookedRoomOutput unbookRoom(RemoveBookedRoomInput input);


}
