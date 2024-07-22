package com.shdv09.appointmentservice.service;

import com.shdv09.appointmentservice.dto.mapper.TimeslotMapper;
import com.shdv09.appointmentservice.dto.response.TimeslotDto;
import com.shdv09.appointmentservice.dto.response.TrainerDto;
import com.shdv09.appointmentservice.dto.mapper.TrainerMapper;
import com.shdv09.appointmentservice.exception.NotFoundException;
import com.shdv09.appointmentservice.repository.TimeSlotRepository;
import com.shdv09.appointmentservice.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;

    private final TrainerMapper trainerMapper;

    private final TimeSlotRepository timeSlotRepository;

    private final TimeslotMapper timeslotMapper;

    @Transactional(readOnly = true)
    @Override
    public List<TrainerDto> findAll() {
        return trainerRepository.findAll().stream()
                .map(trainerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<TimeslotDto> getBusyTimeslots(Long trainerId, LocalDate appointmentDate) {
        if (!trainerRepository.existsById(trainerId)) {
            throw new NotFoundException("Trainer with id=%d not found".formatted(trainerId));
        }
        return timeSlotRepository.findBypKeyWorkoutDateAndTrainerId(appointmentDate, trainerId).stream()
                .map(timeslotMapper::toDto)
                .collect(Collectors.toList());
    }
}
