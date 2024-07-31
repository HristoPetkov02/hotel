package com.tinqinacademy.hotel.core.services.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HotelApiException extends RuntimeException{

    private HttpStatus httpStatus;

    public HotelApiException(String message){
        super(message);
    }

    public HotelApiException(String message, HttpStatus httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }
}
