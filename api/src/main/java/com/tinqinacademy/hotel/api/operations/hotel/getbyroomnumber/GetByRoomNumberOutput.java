package com.tinqinacademy.hotel.api.operations.hotel.getbyroomnumber;

import com.tinqinacademy.hotel.api.base.OperationOutput;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetByRoomNumberOutput implements OperationOutput {
    private String roomId;
}
