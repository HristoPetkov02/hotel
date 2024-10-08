package com.tinqinacademy.hotel.api.operations.system.addroom;

import com.tinqinacademy.hotel.api.base.OperationInput;
import com.tinqinacademy.hotel.api.validation.annotations.ValidBathroomType;
import com.tinqinacademy.hotel.api.validation.annotations.ValidBedSize;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddRoomInput implements OperationInput {
    @Min(value = 1 , message = "Bed count must be at least 1")
    @Max(value = 10, message = "Bed count must be less than 10")
    private Integer bedCount;

    @NotEmpty(message = "Bed sizes are required")
    private List<@ValidBedSize String> bedSizes;

    @NotNull(message = "Bathroom type field must be present")
    @ValidBathroomType
    private String bathroomType;

    @PositiveOrZero(message = "Floor must be positive")
    private Integer floor;

    @NotBlank(message = "Room number is required")
    private String roomNo;

    @Positive(message = "Price must be positive")
    private BigDecimal price;
}
