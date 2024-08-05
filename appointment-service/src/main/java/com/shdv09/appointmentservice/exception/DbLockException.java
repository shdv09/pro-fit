package com.shdv09.appointmentservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DbLockException extends RuntimeException {
    private final String message;
}
