package com.shdv09.appointmentservice.controller;

import com.shdv09.appointmentservice.dto.response.TimeslotDto;
import com.shdv09.appointmentservice.dto.response.TrainerDto;
import com.shdv09.appointmentservice.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
        return trainerService.findAll();
    }

    @GetMapping("/trainers/{id}/timeslots")
    public List<TimeslotDto> getBusyTimeslots(@PathVariable(name = "id") Long trainerId,
            @RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appointmentDate) {
        return trainerService.getBusyTimeslots(trainerId, appointmentDate);
    }
}
