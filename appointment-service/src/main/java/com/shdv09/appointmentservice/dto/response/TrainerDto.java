package com.shdv09.appointmentservice.dto.response;

import com.shdv09.appointmentservice.model.Specialization;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TrainerDto {
    private Long id;

    private String firstName;

    private String lastName;

    private String gender;

    private LocalDate birthDate;

    private Specialization specialization;
}
