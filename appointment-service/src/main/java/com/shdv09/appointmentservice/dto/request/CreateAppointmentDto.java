package com.shdv09.appointmentservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на запись клиента на персональную тренировку")
public class CreateAppointmentDto {
    @NotNull
    @Schema(description = "Идентификатор тренера", example = "2")
    private Long trainerId;

    @NotNull
    @Schema(description = "Идентификатор клиента", example = "1")
    private Long clientId;

    @NotNull
    @Schema(description = "Дата тренировки", type = "string", format = "date", example = "2024-08-27")
    private LocalDate date;

    @NotNull
    @Schema(description = "Час начала тренировки", example = "12")
    private Integer hour;
}
