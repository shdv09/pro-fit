package com.shdv09.appointmentservice.service;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DateFactory {
    public LocalDate generateDate() {
        return LocalDate.now();
    }
}
