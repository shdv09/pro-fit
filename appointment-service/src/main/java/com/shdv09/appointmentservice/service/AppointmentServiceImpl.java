package com.shdv09.appointmentservice.service;

import com.shdv09.appointmentservice.config.AppProperties;
import com.shdv09.appointmentservice.dto.mapper.AppointmentMapper;
import com.shdv09.appointmentservice.dto.request.CreateAppointmentDto;
import com.shdv09.appointmentservice.dto.response.AppointmentDto;
import com.shdv09.appointmentservice.exception.NotFoundException;
import com.shdv09.appointmentservice.exception.RequestValidationException;
import com.shdv09.appointmentservice.exception.TimeslotBusyException;
import com.shdv09.appointmentservice.model.TimeSlot;
import com.shdv09.appointmentservice.model.Trainer;
import com.shdv09.appointmentservice.repository.TimeSlotRepository;
import com.shdv09.appointmentservice.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.shdv09.appointmentservice.model.TimeSlot.TimeSlotPK;

@RequiredArgsConstructor
@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final TrainerRepository trainerRepository;

    private final TimeSlotRepository timeSlotRepository;

    private final AppointmentMapper appointmentMapper;

    private final AppProperties appProperties;

    @Override
    public synchronized AppointmentDto createAppointment(CreateAppointmentDto data) {
        validateCreateAppointmentRequest(data);
        Trainer trainer = trainerRepository.findById(data.getTrainerId())
                .orElseThrow(
                        () -> new NotFoundException("Trainer with id = %d not found".formatted(data.getTrainerId())));
        TimeSlotPK timeSlotPrimaryKey = new TimeSlotPK(data.getTrainerId(), data.getDate(), data.getHour());
        if (timeSlotRepository.findById(timeSlotPrimaryKey).isPresent()) {
            throw new TimeslotBusyException("Timeslot busy, try another time");
        }
        TimeSlot timeslot = new TimeSlot(
                timeSlotPrimaryKey,
                trainer,
                data.getClientId()
        );
        AppointmentDto result;
        try {
            result = appointmentMapper.toDto(timeSlotRepository.save(timeslot));
        } catch (DataIntegrityViolationException e) {
            throw new TimeslotBusyException("Timeslot busy, try another time");
        }
        return result;
    }

    private void validateCreateAppointmentRequest(CreateAppointmentDto data) {
        if (data.getDate().isBefore(LocalDate.now())) {
            throw new RequestValidationException("Invalid date, you can't create appointment in the past");
        }
        if (data.getHour() < appProperties.getOpenHour() || data.getHour() > appProperties.getCloseHour() - 1) {
            throw new RequestValidationException("Invalid hour, choose your workout time between %s and %s o'clock"
                    .formatted(appProperties.getOpenHour(), appProperties.getCloseHour() - 1));
        }
    }
}
