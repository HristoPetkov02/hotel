package com.tinqinacademy.hotel.api.operations.system.deleteroom;

import com.tinqinacademy.hotel.api.base.OperationInput;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteRoomInput implements OperationInput {
    @UUID(message = "Room id must be a valid UUID")
    @NotBlank(message = "Room id is required")
    private String id;
}
