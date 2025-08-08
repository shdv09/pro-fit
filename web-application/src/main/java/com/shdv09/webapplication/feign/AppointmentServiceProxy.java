package com.shdv09.webapplication.feign;

import com.shdv09.webapplication.dto.request.CreateAppointmentDto;
import com.shdv09.webapplication.dto.response.AppointmentDto;
import com.shdv09.webapplication.dto.response.TimeslotDto;
import com.shdv09.webapplication.dto.response.TrainerDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "appointment-service", url = "${services.appointment-service}")
public interface AppointmentServiceProxy {

    @PostMapping("/api/appointment")
    AppointmentDto createAppointment(@RequestBody @Valid CreateAppointmentDto request);

    @GetMapping("/api/trainers")
    List<TrainerDto> getAllTrainers();

    @GetMapping("/api/trainers/{id}")
    TrainerDto findTrainer(@PathVariable(name = "id") Long id);

    @GetMapping("/api/trainers/{id}/timeslots")
    List<TimeslotDto> getBusyTimeslots(@PathVariable(name = "id") Long trainerId,
                                       @RequestParam(name = "date") LocalDate appointmentDate);
}
