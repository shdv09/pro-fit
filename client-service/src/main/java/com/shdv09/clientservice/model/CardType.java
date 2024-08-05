package com.shdv09.clientservice.model;

public enum CardType {
    MORNING("08:00 - 12:00"),
    AFTERNOON("12:00 - 17:00"),
    EVENING("17:00 - 22:00"),
    ALL_DAY("08:00 - 22:00");

    private final String description;

    CardType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
