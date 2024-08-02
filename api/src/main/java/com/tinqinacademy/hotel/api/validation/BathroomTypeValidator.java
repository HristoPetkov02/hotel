package com.tinqinacademy.hotel.api.validation;

import com.tinqinacademy.hotel.api.model.BathroomType;
import com.tinqinacademy.hotel.api.validation.annotations.ValidBathroomType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class BathroomTypeValidator implements ConstraintValidator<ValidBathroomType, String> {
    @Override
    public void initialize(ValidBathroomType constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return true;
        }

        for (BathroomType bathroomType : BathroomType.values()) {
            if (!bathroomType.toString().isEmpty() && bathroomType.toString().equals(s)) {
                return true;
            }
        }

        return false;
    }
}
