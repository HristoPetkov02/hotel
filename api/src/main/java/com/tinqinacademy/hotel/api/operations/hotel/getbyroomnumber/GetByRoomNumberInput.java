package com.tinqinacademy.hotel.api.operations.hotel.getbyroomnumber;

import com.tinqinacademy.hotel.api.base.OperationInput;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetByRoomNumberInput implements OperationInput {
    @NotBlank(message = "Room number is required")
    private String roomNumber;
}
