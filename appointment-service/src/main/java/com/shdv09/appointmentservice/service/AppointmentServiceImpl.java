package com.shdv09.appointmentservice.service;

import com.shdv09.appointmentservice.dto.mapper.AppointmentMapper;
import com.shdv09.appointmentservice.dto.request.CreateAppointmentDto;
import com.shdv09.appointmentservice.dto.response.AppointmentDto;
import com.shdv09.appointmentservice.exception.DbLockException;
import com.shdv09.appointmentservice.exception.NotFoundException;
import com.shdv09.appointmentservice.exception.TimeslotBusyException;
import com.shdv09.appointmentservice.model.TimeSlot;
import com.shdv09.appointmentservice.model.Trainer;
import com.shdv09.appointmentservice.repository.TimeSlotRepository;
import com.shdv09.appointmentservice.repository.TrainerRepository;
import com.shdv09.appointmentservice.validation.CreateAppointmentValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.locks.Lock;

import static com.shdv09.appointmentservice.model.TimeSlot.TimeSlotPK;

@RequiredArgsConstructor
@Service
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final TrainerRepository trainerRepository;

    private final TimeSlotRepository timeSlotRepository;

    private final AppointmentMapper appointmentMapper;

    private final CreateAppointmentValidator createAppointmentValidator;

    private final LockRegistry lockRegistry;

    @Transactional
    @Override
    public AppointmentDto createAppointment(CreateAppointmentDto data) {
        Long threadId = Thread.currentThread().getId();
        createAppointmentValidator.validateRequest(data);
        Trainer trainer = trainerRepository.findById(data.getTrainerId()).orElseThrow(
                        () -> new NotFoundException("Trainer with id = %d not found".formatted(data.getTrainerId())));
        TimeSlotPK timeSlotPrimaryKey = new TimeSlotPK(data.getTrainerId(), data.getDate(), data.getHour());
        Lock lock = lockRegistry.obtain(timeSlotPrimaryKey.toString());
        if (lock.tryLock()) {
            log.info("Obtained lock. Thread: %d, key: %s".formatted(threadId, timeSlotPrimaryKey));
                try {
                    if (timeSlotRepository.findById(timeSlotPrimaryKey).isPresent()) {
                        throw new TimeslotBusyException("Timeslot busy, try another time");
                    }
                    TimeSlot timeslot = new TimeSlot(timeSlotPrimaryKey,trainer,data.getClientId());
                    return appointmentMapper.toDto(timeSlotRepository.save(timeslot));
                } catch (DataIntegrityViolationException e) {
                    throw new TimeslotBusyException("Timeslot busy, try another time");
                } finally {
                    log.info("Released lock. Thread: %d, key: %s".formatted(threadId, timeSlotPrimaryKey));
                    lock.unlock();
                }
            } else {
                throw new DbLockException("Error acquiring lock for PK: %s".formatted(timeSlotPrimaryKey));
            }
    }
}
