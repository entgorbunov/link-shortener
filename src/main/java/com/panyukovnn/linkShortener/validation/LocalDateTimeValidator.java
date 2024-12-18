package com.panyukovnn.linkshortener.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LocalDateTimeValidator implements ConstraintValidator<ValidFutureDateTime, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime dateTime, ConstraintValidatorContext constraintValidatorContext) {
        if (dateTime == null) {
            return true;
        }
        return dateTime.isAfter(LocalDateTime.now());
    }
}
