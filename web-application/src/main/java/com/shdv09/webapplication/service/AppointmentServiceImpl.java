package com.shdv09.webapplication.service;

import com.shdv09.webapplication.dto.request.CreateAppointmentDto;
import com.shdv09.webapplication.dto.response.AppointmentDto;
import com.shdv09.webapplication.feign.AppointmentServiceProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {
    private static final String LOG_CODE = "APPOINTMENT";

    private final AppointmentServiceProxy appointmentServiceProxy;

    @Override
    public AppointmentDto createAppointment(CreateAppointmentDto request) {
        log.info("{}. Creating appointment. Request: {}", LOG_CODE, request);
        return appointmentServiceProxy.createAppointment(request);
    }
}
