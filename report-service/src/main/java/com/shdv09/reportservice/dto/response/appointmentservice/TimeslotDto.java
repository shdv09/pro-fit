package com.shdv09.reportservice.dto.response.appointmentservice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Объект DTO промежутка времени тренировки")
public class TimeslotDto {
    @Schema(description = "Дата иренировки")
    private LocalDate workoutDate;

    @Schema(description = "Час начала тренировки")
    private Integer workoutHour;
}
