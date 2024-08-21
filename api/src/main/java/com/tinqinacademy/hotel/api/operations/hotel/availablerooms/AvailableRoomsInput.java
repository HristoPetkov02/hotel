package com.tinqinacademy.hotel.api.operations.hotel.availablerooms;

import com.tinqinacademy.hotel.api.base.OperationInput;
import com.tinqinacademy.hotel.api.base.OperationOutput;
import com.tinqinacademy.hotel.api.validation.annotations.ValidBathroomType;
import com.tinqinacademy.hotel.api.validation.annotations.ValidBedSize;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvailableRoomsInput implements OperationInput {
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @Min(value = 1 , message = "Bed count must be at least 1")
    @Max(value = 10, message = "Bed count must be less than 10")
    private Integer bedCount;

    @ValidBedSize
    private String bedSize;

    @ValidBathroomType
    private String bathroomType;
}
