package com.tinqinacademy.hotel.core.services.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import com.tinqinacademy.hotel.api.model.Error;

import java.util.List;

@Getter
public class HotelValidationException extends RuntimeException{
    private final List<Error> errors;
    private final HttpStatus status;

    public HotelValidationException(String message, List<Error> errors, HttpStatus status){
        super(message);
        this.errors = errors;
        this.status = status;
    }
}
