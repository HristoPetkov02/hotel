package com.tinqinacademy.hotel.api.operations.availablerooms;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvailableRoomsOutput {
    //листа на идитата на стаите получени от проверката дали са свободни
    private List<String> ids;
}
