package com.shdv09.appointmentservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RequestValidationException extends RuntimeException {
    private final String message;
}
