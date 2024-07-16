package com.shdv09.visitservice.dto.response.clientservice;

import java.time.LocalTime;

public enum CardType {
    MORNING("08:00:00", "12:00:00"),
    AFTERNOON("12:00:00", "17:00:00"),
    EVENING("17:00:00", "22:00:00"),
    ALL_DAY("08:00:00", "22:00:00");

    private final LocalTime start;
    private final LocalTime end;

    CardType(String start, String end) {
        this.start = LocalTime.parse(start);
        this.end = LocalTime.parse(end);
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }
}
