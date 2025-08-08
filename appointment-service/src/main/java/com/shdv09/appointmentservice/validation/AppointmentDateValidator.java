package com.shdv09.appointmentservice.validation;

import com.shdv09.appointmentservice.service.DateFactory;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class AppointmentDateValidator implements ConstraintValidator<ValidAppointmentDate, LocalDate> {

    private final DateFactory dateFactory;

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value != null && !value.isBefore(dateFactory.generateDate());
    }
}
