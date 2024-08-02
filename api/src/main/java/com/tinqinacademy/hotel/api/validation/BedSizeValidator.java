package com.tinqinacademy.hotel.api.validation;

import com.tinqinacademy.hotel.api.model.BedSize;
import com.tinqinacademy.hotel.api.validation.annotations.ValidBedSize;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BedSizeValidator implements ConstraintValidator<ValidBedSize, String> {
    @Override
    public void initialize(ValidBedSize constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return true;
        }

        for (BedSize bedSize : BedSize.values()) {
            if (!bedSize.toString().isEmpty() && bedSize.toString().equals(s)) {
                return true;
            }
        }

        return false;
    }
}
