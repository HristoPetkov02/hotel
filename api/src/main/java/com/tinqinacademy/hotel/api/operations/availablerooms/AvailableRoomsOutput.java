package com.tinqinacademy.hotel.api.operations.availablerooms;

import com.tinqinacademy.hotel.api.base.OperationOutput;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvailableRoomsOutput implements OperationOutput {
    private List<String> ids;
}
