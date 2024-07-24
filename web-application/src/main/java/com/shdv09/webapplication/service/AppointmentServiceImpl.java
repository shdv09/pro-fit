package com.shdv09.webapplication.service;

import com.shdv09.webapplication.dto.request.CreateAppointmentDto;
import com.shdv09.webapplication.dto.response.AppointmentDto;
import com.shdv09.webapplication.feign.AppointmentServiceProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentServiceProxy appointmentServiceProxy;

    @Override
    public AppointmentDto createAppointment(CreateAppointmentDto request) {
        return appointmentServiceProxy.createAppointment(request);
    }
}
