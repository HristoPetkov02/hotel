package com.tinqinacademy.hotel.api.operations.system.partiallyupdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.tinqinacademy.hotel.api.base.OperationInput;
import com.tinqinacademy.hotel.api.validation.annotations.ValidBathroomType;
import com.tinqinacademy.hotel.api.validation.annotations.ValidBedSize;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.UUID;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PartiallyUpdateInput implements OperationInput {
    @JsonIgnore
    @UUID(message = "Room ID must be a valid UUID")
    private String roomId;

    @Min(value = 1, message = "Bed count must be at least 1")
    @Max(value = 10, message = "Bed count must be less than 10")
    private Integer bedCount;

    private List<@ValidBedSize String> bedSizes;

    @ValidBathroomType
    private String bathroomType;
    private String roomNo;

    @Positive(message = "Price must be positive")
    private BigDecimal price;
}
