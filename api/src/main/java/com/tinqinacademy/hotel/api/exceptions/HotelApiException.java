package com.tinqinacademy.hotel.api.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HotelApiException extends RuntimeException{

    private final HttpStatus httpStatus;

    public HotelApiException(String message, HttpStatus httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }
}
