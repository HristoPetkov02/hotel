package com.tinqinacademy.hotel.api.operations.hotel.unbookroom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinqinacademy.hotel.api.base.OperationInput;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UnbookRoomInput implements OperationInput {
    @JsonIgnore
    @NotNull(message = "Booking ID is required")
    @UUID(message = "Booking ID must be a valid UUID")
    private String bookingId;

    @NotBlank(message = "User ID is required")
    @UUID(message = "User ID must be a valid UUID")
    private String userId;
}
