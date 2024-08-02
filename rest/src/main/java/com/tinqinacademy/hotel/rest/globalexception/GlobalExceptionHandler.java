package com.tinqinacademy.hotel.rest.globalexception;


import com.tinqinacademy.hotel.api.model.ErrorWrapper;
import com.tinqinacademy.hotel.core.services.exceptions.HotelApiException;
import com.tinqinacademy.hotel.api.interfaces.ExceptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(HotelApiException.class)
    public ResponseEntity<?> handleHotelApiException(HotelApiException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex){
        String response = ex.getMessage();
        if (ex.getCause()!=null)
            response+="\nCause: "+ex.getCause();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
}

