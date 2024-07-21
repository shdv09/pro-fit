package com.shdv09.appointmentservice.dto.mapper;

import com.shdv09.appointmentservice.dto.response.TrainerDto;
import com.shdv09.appointmentservice.model.Trainer;
import org.springframework.stereotype.Component;

@Component
public class TrainerMapper {
    public TrainerDto toDto(Trainer entity) {
        TrainerDto result = new TrainerDto();
        result.setId(entity.getId());
        result.setFirstName(entity.getFirstName());
        result.setLastName(entity.getLastName());
        result.setGender(entity.getGender());
        result.setBirthDate(entity.getBirthDate());
        result.setSpecialization(entity.getSpecialization());
        return result;
    }
}
