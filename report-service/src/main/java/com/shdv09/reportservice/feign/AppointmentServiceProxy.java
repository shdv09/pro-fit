package com.shdv09.reportservice.feign;

import com.shdv09.reportservice.dto.response.appointmentservice.TimeslotDto;
import com.shdv09.reportservice.dto.response.appointmentservice.TrainerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "appointment-service", url = "${services.appointment-service}")
public interface AppointmentServiceProxy {

    @GetMapping("/api/trainers")
    List<TrainerDto> getAllTrainers();

    @GetMapping("/api/trainers/{id}")
    TrainerDto findTrainer(@PathVariable(name = "id") Long id);

    @GetMapping("/api/trainers/{id}/timeslots")
    List<TimeslotDto> getBusyTimeslots(@PathVariable(name = "id") Long trainerId,
                                       @RequestParam(name = "date") LocalDate appointmentDate);
}
