package com.shdv09.webapplication.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Data
public class TrainerScheduleDto {
    private LocalDate date;

    private Set<Integer> busyHours = new TreeSet<>();
}
