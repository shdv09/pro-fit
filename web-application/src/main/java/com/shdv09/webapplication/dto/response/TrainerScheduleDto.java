package com.shdv09.webapplication.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Data
@Schema(description = "Объект DTO расписания тренера")
public class TrainerScheduleDto {
    @Schema(description = "Дата расписания")
    private LocalDate date;

    @Schema(description = "Занятые часы")
    private Set<Integer> busyHours = new TreeSet<>();
}
