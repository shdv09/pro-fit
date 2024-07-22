package com.shdv09.appointmentservice.validation;

import com.shdv09.appointmentservice.config.AppProperties;
import com.shdv09.appointmentservice.dto.request.CreateAppointmentDto;
import com.shdv09.appointmentservice.exception.RequestValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CreateAppointmentValidator {

    private final AppProperties appProperties;

    public void validateRequest(CreateAppointmentDto data) {
        if (data.getDate().isBefore(LocalDate.now())) {
            throw new RequestValidationException("Invalid date, you can't create appointment in the past");
        }
        if (data.getHour() < appProperties.getOpenHour() || data.getHour() > appProperties.getCloseHour() - 1) {
            throw new RequestValidationException("Invalid hour, choose your workout time between %s and %s o'clock"
                    .formatted(appProperties.getOpenHour(), appProperties.getCloseHour() - 1));
        }
    }
}
