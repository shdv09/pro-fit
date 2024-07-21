package com.shdv09.appointmentservice.service;

import com.shdv09.appointmentservice.dto.response.TimeslotDto;
import com.shdv09.appointmentservice.dto.response.TrainerDto;

import java.time.LocalDate;
import java.util.List;

public interface TrainerService {
    List<TrainerDto> findAll();

    List<TimeslotDto> getBusyTimeslots(Long trainerId, LocalDate appointmentDate);
}
