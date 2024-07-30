package com.tinqinacademy.hotel.api.operations.availablerooms;

import com.tinqinacademy.hotel.api.base.OperationInput;
import com.tinqinacademy.hotel.api.base.OperationOutput;
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
    //това е за проверка на всички налични стаи с тези данни
    private LocalDate startDate;
    private LocalDate endDate;
    private Optional<Integer> bedCount;
    private Optional<String> bedSize;
    private Optional<String> bathroomType;
}
