package com.shdv09.appointmentservice.service;

import com.shdv09.appointmentservice.dto.request.CreateAppointmentDto;
import com.shdv09.appointmentservice.dto.response.AppointmentDto;

public interface AppointmentService {
    AppointmentDto createAppointment(CreateAppointmentDto data);
}
