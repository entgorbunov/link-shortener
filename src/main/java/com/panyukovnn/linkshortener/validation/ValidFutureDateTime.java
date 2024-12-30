package com.panyukovnn.linkshortener.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LocalDateTimeValidator.class)
public @interface ValidFutureDateTime {

    String message() default "Дата должна быть в будущем";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
