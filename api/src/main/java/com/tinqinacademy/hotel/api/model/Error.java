package com.tinqinacademy.hotel.api.model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Error {
    private String field;
    private String message;
}
