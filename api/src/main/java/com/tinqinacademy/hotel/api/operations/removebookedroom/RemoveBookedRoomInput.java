package com.tinqinacademy.hotel.api.operations.removebookedroom;

import com.tinqinacademy.hotel.api.base.OperationInput;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RemoveBookedRoomInput implements OperationInput {
    //това е входа за премахване на резервацията на стая
    private String bookingId;
}
