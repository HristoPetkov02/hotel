package com.tinqinacademy.hotel.api.operations.system.updateroom;

import com.tinqinacademy.hotel.api.base.OperationOutput;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateRoomOutput implements OperationOutput {
    private String id;
}
