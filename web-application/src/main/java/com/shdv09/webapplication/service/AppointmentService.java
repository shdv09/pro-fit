package com.shdv09.webapplication.service;

import com.shdv09.webapplication.dto.request.CreateAppointmentDto;
import com.shdv09.webapplication.dto.response.AppointmentDto;

public interface AppointmentService {
    AppointmentDto createAppointment(CreateAppointmentDto request);
}
