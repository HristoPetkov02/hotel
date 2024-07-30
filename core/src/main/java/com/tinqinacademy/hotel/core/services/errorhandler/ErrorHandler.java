package com.tinqinacademy.hotel.core.services.errorhandler;

import com.tinqinacademy.hotel.api.model.Error;
import com.tinqinacademy.hotel.api.model.ErrorWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ErrorHandler {

    public ErrorWrapper handle(Throwable throwable){
        List<Error> errors = new ArrayList<>();
        errors.add(Error.builder()
                        .message(throwable.getMessage())
                        .build());

        ErrorWrapper errorWrapper = ErrorWrapper.builder()
                .errors(errors)
                .errorCode(HttpStatus.BAD_REQUEST)
                .build();
        return errorWrapper;
    }
}
