package com.tinqinacademy.hotel.core.services.errorhandler;

import com.tinqinacademy.hotel.api.interfaces.ErrorHandlerService;
import com.tinqinacademy.hotel.api.model.Error;
import com.tinqinacademy.hotel.api.model.ErrorWrapper;
import com.tinqinacademy.hotel.api.exceptions.HotelApiException;
import com.tinqinacademy.hotel.api.exceptions.HotelValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

@Slf4j
@Component
public class ErrorHandlerServiceImpl implements ErrorHandlerService {
    @Override
    public ErrorWrapper handle(Throwable throwable) {
        return Match(throwable).of(
                Case($(instanceOf(HotelApiException.class)), this::handleHotelApiException),
                Case($(instanceOf(HotelValidationException.class)), this::handleHotelValidationException),
                Case($(), this::handleDefaultException)
        );
    }

    private ErrorWrapper handleHotelApiException(HotelApiException ex) {
        return ErrorWrapper.builder()
                .errorCode(ex.getHttpStatus())
                .errors(
                        List.of(
                                Error.builder()
                                        .message(ex.getMessage())
                                        .build()))
                .build();
    }

    private ErrorWrapper handleHotelValidationException(HotelValidationException ex) {
        return ErrorWrapper.builder()
                .errorCode(ex.getStatus())
                .errors(ex.getErrors())
                .build();
    }

    private ErrorWrapper handleDefaultException(Throwable ex) {
        return ErrorWrapper.builder()
                .errorCode(HttpStatus.BAD_REQUEST)
                .errors(
                        List.of(
                                Error.builder()
                                        .message(ex.getMessage())
                                        .build()))
                .build();
    }
}
