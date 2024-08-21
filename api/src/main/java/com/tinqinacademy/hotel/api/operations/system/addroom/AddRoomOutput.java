package com.tinqinacademy.hotel.api.operations.system.addroom;

import com.tinqinacademy.hotel.api.base.OperationOutput;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddRoomOutput implements OperationOutput {
    private String id;
}
