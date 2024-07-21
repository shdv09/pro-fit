package com.shdv09.appointmentservice.controller;

import com.shdv09.appointmentservice.dto.request.CreateAppointmentDto;
import com.shdv09.appointmentservice.dto.response.AppointmentDto;
import com.shdv09.appointmentservice.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/appointment")
    public AppointmentDto createAppointment(@RequestBody @Valid CreateAppointmentDto request) {
        return appointmentService.createAppointment(request);
    }
}
