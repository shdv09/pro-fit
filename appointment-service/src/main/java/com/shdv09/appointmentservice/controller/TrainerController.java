package com.shdv09.appointmentservice.controller;

import com.shdv09.appointmentservice.dto.response.TimeslotDto;
import com.shdv09.appointmentservice.dto.response.TrainerDto;
import com.shdv09.appointmentservice.service.TrainerService;
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
        return trainerService.findAll();
    }

    @GetMapping("/trainers/{id}")
    @Operation(summary = "Метод поиска тренера по идентификатору")
    public TrainerDto findTrainer(
            @Parameter(description = "Идентификатор тренера") @PathVariable(name = "id") Long id) {
        return trainerService.findTrainer(id);
    }

    @GetMapping("/trainers/{id}/timeslots")
    @Operation(summary = "Метод получения расписания тренера на определённую дату")
    public List<TimeslotDto> getBusyTimeslots(
            @Parameter(description = "Идентификатор тренера") @PathVariable(name = "id") Long trainerId,
            @Parameter(description = "Дата расписания") @RequestParam(name = "date") LocalDate appointmentDate) {
        return trainerService.getBusyTimeslots(trainerId, appointmentDate);
    }
}
