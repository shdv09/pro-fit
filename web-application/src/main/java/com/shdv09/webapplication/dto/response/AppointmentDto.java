package com.shdv09.webapplication.dto.response;

import lombok.Data;

@Data
public class AppointmentDto {
    private TrainerDto trainer;

    private TimeslotDto timeslot;
}
