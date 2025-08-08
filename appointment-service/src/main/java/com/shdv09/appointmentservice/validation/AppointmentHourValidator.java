package com.shdv09.appointmentservice.validation;

import com.shdv09.appointmentservice.config.AppProperties;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentHourValidator implements ConstraintValidator<ValidAppointmentHour, Integer> {

    private final AppProperties appProperties;

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value != null && value >= appProperties.getOpenHour() && value <= appProperties.getCloseHour() - 1;
    }
}
