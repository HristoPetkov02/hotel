package com.tinqinacademy.hotel.api.operations.getroom;

import com.tinqinacademy.hotel.api.base.OperationInput;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetRoomInput implements OperationInput {
    @NotNull(message = "Room ID is required")
    private String roomId;
}
