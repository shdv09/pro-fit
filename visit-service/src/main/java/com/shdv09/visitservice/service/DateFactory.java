package com.shdv09.visitservice.service;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DateFactory {
    public LocalDateTime generateDate() {
        return LocalDateTime.now();
    }
}
