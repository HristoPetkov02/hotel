package com.tinqinacademy.hotel.api.operations.updateroom;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Builder(toBuilder = true)
public class UpdateRoomInput implements OperationInput {
    //това са входните данни за обновяване на данните за стая
    @JsonIgnore
    private String roomId;

    @Min(value = 1 , message = "Bed count must be at least 1")
    @Max(value = 10, message = "Bed count must be less than 10")
    private Integer bedCount;

    @NotEmpty(message = "Bed Sizes is required")
    private List<@ValidBedSize String> bedSizes;

    @NotBlank(message = "Bathroom Type is required")
    @ValidBathroomType
    private String bathroomType;

    @NotBlank(message = "Room number is required")
    private String roomNo;

    @Positive(message = "Price must be positive")
    private BigDecimal price;
}
