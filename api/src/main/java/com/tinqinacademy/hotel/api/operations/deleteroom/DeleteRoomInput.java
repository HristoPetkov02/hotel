package com.tinqinacademy.hotel.api.operations.deleteroom;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteRoomInput {
    //това са входните данни за изтриване на стаята
    private String id;
}
