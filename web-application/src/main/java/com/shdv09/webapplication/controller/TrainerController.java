package com.shdv09.webapplication.controller;

import com.shdv09.webapplication.dto.response.TrainerDto;
import com.shdv09.webapplication.dto.response.TrainerScheduleDto;
import com.shdv09.webapplication.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Trainer Controller", description = "Контроллер для работы с тренерами клуба")
@SecurityRequirement(name = "JWT")
public class TrainerController {
    private final TrainerService trainerService;

    @GetMapping("/trainers")
    @Operation(summary = "Метод получения списка всех тренеров")
    public List<TrainerDto> getAllTrainers() {
        return trainerService.getAllTrainers();
    }

    @GetMapping("/trainers/{id}")
    @Operation(summary = "Метод поиска тренера по идентификатору")
    public TrainerDto findTrainer(
            @Parameter(description = "Идентификатор тренера") @PathVariable(name = "id") Long trainerId) {
        return trainerService.findTrainer(trainerId);
    }

    @GetMapping("/trainers/{id}/schedule")
    @Operation(summary = "Метод получения расписания тренера на определённую дату")
    public TrainerScheduleDto getTrainerSchedule(
            @PathVariable(name = "id") Long trainerId,
            @Parameter(description = "Дата расписания") @RequestParam(name = "date") LocalDate date) {
        return trainerService.getTrainerSchedule(trainerId, date);
    }
}
