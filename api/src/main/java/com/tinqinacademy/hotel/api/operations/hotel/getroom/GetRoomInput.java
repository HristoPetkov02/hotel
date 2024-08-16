package com.tinqinacademy.hotel.api.operations.hotel.getroom;

import com.tinqinacademy.hotel.api.base.OperationInput;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetRoomInput implements OperationInput {
    @NotNull(message = "Room ID is required")
    @UUID(message = "Room ID must be a valid UUID")
    private String roomId;
}
