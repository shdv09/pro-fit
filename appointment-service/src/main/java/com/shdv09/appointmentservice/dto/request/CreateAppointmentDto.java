package com.shdv09.appointmentservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateAppointmentDto {
    @NotNull
    private Long trainerId;
    @NotNull
    private Long clientId;
    @NotNull
    private LocalDate date;
    @NotNull
    private Integer hour;
}
