package com.shdv09.appointmentservice.dto.mapper;

import com.shdv09.appointmentservice.dto.response.TimeslotDto;
import com.shdv09.appointmentservice.model.TimeSlot;
import org.springframework.stereotype.Component;

@Component
public class TimeslotMapper {
    public TimeslotDto toDto(TimeSlot entity) {
        TimeslotDto result = new TimeslotDto();
        result.setWorkoutDate(entity.getPKey().getWorkoutDate());
        result.setWorkoutHour(entity.getPKey().getWorkoutHour());
        return result;
    }
}
