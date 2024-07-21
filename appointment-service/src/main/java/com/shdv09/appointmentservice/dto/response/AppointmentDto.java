package com.shdv09.appointmentservice.dto.response;

import lombok.Data;

@Data
public class AppointmentDto {
    private TrainerDto trainer;

    private TimeslotDto timeslot;
}
