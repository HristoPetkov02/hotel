package com.tinqinacademy.hotel.api.operations.removebookedroom;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RemoveBookedRoomInput {
    //това е входа за премахване на резервацията на стая
    private String bookingId;
}
