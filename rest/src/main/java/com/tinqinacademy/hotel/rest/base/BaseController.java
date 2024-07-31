package com.tinqinacademy.hotel.rest.base;

import com.tinqinacademy.hotel.api.base.OperationOutput;
import com.tinqinacademy.hotel.api.model.ErrorWrapper;
import io.vavr.control.Either;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {
    protected<O extends OperationOutput> ResponseEntity<?> handleWithCode(Either<ErrorWrapper,O> result, HttpStatus status){
        if (result.isRight()) {
            O output = result.get();
            return new ResponseEntity<>(output, status);
        } else {
            return error(result);
        }
    }

    protected<O extends OperationOutput> ResponseEntity<?> handle(Either<ErrorWrapper,O> result){
        if (result.isRight()) {
            O output = result.get();
            return new ResponseEntity<>(output, HttpStatus.OK);
        } else {
            return error(result);
        }
    }

    private<O extends OperationOutput> ResponseEntity<?> error(Either<ErrorWrapper,O> result){
        ErrorWrapper errorWrapper = result.getLeft();
        return new ResponseEntity<>(errorWrapper.getErrors(), errorWrapper.getErrorCode());
    }
}
