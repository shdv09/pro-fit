package com.shdv09.appointmentservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AppointmentHourValidator.class)
public @interface ValidAppointmentHour {
    String message() default  "Invalid appointment hour";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
