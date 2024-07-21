package com.shdv09.appointmentservice.dto.mapper;

import com.shdv09.appointmentservice.dto.response.AppointmentDto;
import com.shdv09.appointmentservice.dto.response.TimeslotDto;
import com.shdv09.appointmentservice.model.TimeSlot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentMapper {

    private final TrainerMapper trainerMapper;

    public AppointmentDto toDto(TimeSlot entity) {
        AppointmentDto result = new AppointmentDto();
        result.setTrainer(trainerMapper.toDto(entity.getTrainer()));
        result.setTimeslot(new TimeslotDto(
                entity.getPKey().getWorkoutDate(),
                entity.getPKey().getWorkoutHour()));
        return result;
    }
}
