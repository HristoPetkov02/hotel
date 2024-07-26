package com.tinqinacademy.hotel.api.interfaces;


import com.tinqinacademy.hotel.api.model.ErrorWrapper;
import org.springframework.web.bind.MethodArgumentNotValidException;

public interface ExceptionService {
    ErrorWrapper handleException(MethodArgumentNotValidException ex);
}
