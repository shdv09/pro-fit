package com.shdv09.webapplication.service;

import com.shdv09.webapplication.dto.response.TimeslotDto;
import com.shdv09.webapplication.dto.response.TrainerDto;
import com.shdv09.webapplication.dto.response.TrainerScheduleDto;
import com.shdv09.webapplication.feign.AppointmentServiceProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerServiceImpl implements TrainerService {
    private static final String LOG_CODE = "TRAINER";

    private final AppointmentServiceProxy appointmentServiceProxy;

    @Override
    public List<TrainerDto> getAllTrainers() {
        log.info("{}. Getting trainers list", LOG_CODE);
        return appointmentServiceProxy.getAllTrainers();
    }

    @Override
    public TrainerDto findTrainer(Long trainerId) {
        log.info("{}. Finding trainer by id", LOG_CODE);
        return appointmentServiceProxy.findTrainer(trainerId);
    }

    @Override
    public TrainerScheduleDto getTrainerSchedule(Long trainerId, LocalDate date) {
        log.info("{}. Getting schedule for trainer", LOG_CODE);
        TrainerScheduleDto result = new TrainerScheduleDto();
        result.setDate(date);
        List<TimeslotDto> busyTimeslots = appointmentServiceProxy.getBusyTimeslots(trainerId, date);
        busyTimeslots.forEach(slot -> result.getBusyHours().add(slot.getWorkoutHour()));
        return result;
    }
}
