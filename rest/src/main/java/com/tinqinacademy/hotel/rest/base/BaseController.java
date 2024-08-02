package com.tinqinacademy.hotel.rest.base;

import com.tinqinacademy.hotel.api.base.OperationOutput;
import com.tinqinacademy.hotel.api.model.ErrorWrapper;
import io.vavr.control.Either;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {
    protected<O extends OperationOutput> ResponseEntity<?> handleWithCode(Either<ErrorWrapper,O> result, HttpStatus status){
        return result.isLeft() ? error(result) : new ResponseEntity<>(result.get(), status);
    }

    protected<O extends OperationOutput> ResponseEntity<?> handle(Either<ErrorWrapper,O> result){
        return result.isLeft() ? error(result) : new ResponseEntity<>(result.get(), HttpStatus.OK);
    }

    private<O extends OperationOutput> ResponseEntity<?> error(Either<ErrorWrapper,O> result){
        return new ResponseEntity<>(result.getLeft().getErrors(), result.getLeft().getErrorCode());
    }
}
