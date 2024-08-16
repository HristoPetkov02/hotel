package com.tinqinacademy.hotel.api.operations.system.partiallyupdate;

import com.tinqinacademy.hotel.api.base.OperationOutput;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartiallyUpdateOutput implements OperationOutput {
    private String id;
}
