package com.shdv09.appointmentservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeslotDto {
    private LocalDate workoutDate;
    private Integer workoutHour;
}
