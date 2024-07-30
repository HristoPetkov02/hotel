package com.tinqinacademy.hotel.api.operations.partiallyupdate;

import com.tinqinacademy.hotel.api.base.OperationOutput;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartiallyUpdateOutput implements OperationOutput {
    //това са изходните данни за обновяване на част от данните за стая
    private String id;
}
