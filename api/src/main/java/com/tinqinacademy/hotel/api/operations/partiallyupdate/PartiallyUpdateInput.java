package com.tinqinacademy.hotel.api.operations.partiallyupdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class PartiallyUpdateInput {
    //това са входните данни за обновяване на част от данните за стая
    @JsonIgnore
    private String roomId;

    private Integer bedCount;
    private List<String> bedSizes;
    private String bathroomType;
    private String roomNo;

    @Positive(message = "Price must be positive")
    private BigDecimal price;
}
