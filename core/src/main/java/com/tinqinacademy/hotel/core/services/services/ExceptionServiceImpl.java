package com.tinqinacademy.hotel.core.services.services;


import com.tinqinacademy.hotel.api.model.ErrorWrapper;
import com.tinqinacademy.hotel.api.interfaces.ExceptionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.tinqinacademy.hotel.api.model.Error;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExceptionServiceImpl implements ExceptionService {
    @Override
    public ErrorWrapper handleException(MethodArgumentNotValidException ex){
        List<Error> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.add(
                        Error.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .build()));


        //ex.getClass().getMethod("getFieldErrors");



        ErrorWrapper errorWrapper;
        errorWrapper = ErrorWrapper.builder()
                .errors(errors)
                .errorCode(HttpStatus.BAD_REQUEST)
                .build();
        return errorWrapper;
    }



}
