package com.shdv09.webapplication.service;

import com.shdv09.webapplication.dto.response.TrainerDto;
import com.shdv09.webapplication.dto.response.TrainerScheduleDto;

import java.time.LocalDate;
import java.util.List;

public interface TrainerService {
    List<TrainerDto> getAllTrainers();

    TrainerDto findTrainer(Long trainerId);

    TrainerScheduleDto getTrainerSchedule(Long trainerId, LocalDate date);
}
