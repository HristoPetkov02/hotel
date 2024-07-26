package com.tinqinacademy.hotel.api.operations.getroom;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetRoomInput {
    //това е инпута за търсене на стая по Id
    private String roomId;
}
