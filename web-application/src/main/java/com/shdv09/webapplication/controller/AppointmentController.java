package com.shdv09.webapplication.controller;

import com.shdv09.webapplication.dto.request.CreateAppointmentDto;
import com.shdv09.webapplication.dto.response.AppointmentDto;
import com.shdv09.webapplication.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Appointment Controller", description = "Контроллер для записи на персональную тренировку")
@SecurityRequirement(name = "JWT")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping("/appointment")
    @Operation(summary = "Метод записи на персональную тренировку")
    public AppointmentDto createAppointment(@RequestBody @Valid CreateAppointmentDto request) {
        return appointmentService.createAppointment(request);
    }
}
