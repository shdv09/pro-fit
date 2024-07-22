package com.shdv09.visitservice.service;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DateFactory {
    public Date generateDate() {
        return new Date();
    }
}
