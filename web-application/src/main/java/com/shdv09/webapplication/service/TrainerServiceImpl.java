package com.shdv09.webapplication.service;

import com.shdv09.webapplication.dto.response.TimeslotDto;
import com.shdv09.webapplication.dto.response.TrainerDto;
import com.shdv09.webapplication.dto.response.TrainerScheduleDto;
import com.shdv09.webapplication.feign.AppointmentServiceProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {
    private final AppointmentServiceProxy appointmentServiceProxy;

    @Override
    public List<TrainerDto> getAllTrainers() {
        return appointmentServiceProxy.getAllTrainers();
    }

    @Override
    public TrainerDto findTrainer(Long trainerId) {
        return appointmentServiceProxy.findTrainer(trainerId);
    }

    @Override
    public TrainerScheduleDto getTrainerSchedule(Long trainerId, LocalDate date) {
        TrainerScheduleDto result = new TrainerScheduleDto();
        result.setDate(date);
        List<TimeslotDto> busyTimeslots = appointmentServiceProxy.getBusyTimeslots(trainerId, date);
        busyTimeslots.forEach(slot -> result.getBusyHours().add(slot.getWorkoutHour()));
        return result;
    }
}
