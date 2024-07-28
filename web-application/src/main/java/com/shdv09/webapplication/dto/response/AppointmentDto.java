package com.shdv09.webapplication.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Ответ метода записи клиента на персональную тренировку")
public class AppointmentDto {
    @Schema(description = "Объект DTO тренера")
    private TrainerDto trainer;

    @Schema(description = "Объект DTO интервала времени тренировки")
    private TimeslotDto timeslot;
}
