package com.shdv09.appointmentservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
