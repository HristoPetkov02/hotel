package com.tinqinacademy.hotel.api.interfaces;

import com.tinqinacademy.hotel.api.model.ErrorWrapper;

public interface ErrorHandlerService {
    ErrorWrapper handle(Throwable throwable);
}
