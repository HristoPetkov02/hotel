package com.tinqinacademy.hotel.api.operations.removebookedroom;

import com.tinqinacademy.hotel.api.base.OperationInput;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RemoveBookedRoomInput implements OperationInput {
    @NotNull(message = "Booking ID is required")
    private String bookingId;
}
