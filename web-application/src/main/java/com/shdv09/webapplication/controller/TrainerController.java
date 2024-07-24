package com.shdv09.webapplication.controller;

import com.shdv09.webapplication.dto.response.TrainerDto;
import com.shdv09.webapplication.dto.response.TrainerScheduleDto;
import com.shdv09.webapplication.service.TrainerService;
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
public class TrainerController {
    private final TrainerService trainerService;

    @GetMapping("/trainers")
    public List<TrainerDto> getAllTrainers() {
        return trainerService.getAllTrainers();
    }

    @GetMapping("/trainers/{id}")
    public TrainerDto findTrainer(@PathVariable(name = "id") Long trainerId) {
        return trainerService.findTrainer(trainerId);
    }

    @GetMapping("/trainers/{id}/schedule")
    public TrainerScheduleDto getTrainerSchedule(@PathVariable(name = "id") Long trainerId,
                                                 @RequestParam(name = "date") LocalDate date) {
        return trainerService.getTrainerSchedule(trainerId, date);
    }
}
