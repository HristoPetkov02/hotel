package com.tinqinacademy.hotel.api.base;

import com.tinqinacademy.hotel.api.model.ErrorWrapper;
import io.vavr.control.Either;

public interface OperationProcessor <I extends OperationInput, O extends OperationOutput>{
    Either<ErrorWrapper,O> process(I input);
}
