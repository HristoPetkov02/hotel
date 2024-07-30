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
    //листа на идитата на стаите получени от проверката дали са свободни
    private List<String> ids;
}
