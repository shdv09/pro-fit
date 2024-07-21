package com.shdv09.appointmentservice.exception;

import com.shdv09.appointmentservice.dto.response.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorDto notFoundException(NotFoundException e) {
        log.error("Object not found: {}", e.getMessage());
        return new ErrorDto(e.getMessage());
    }

    @ExceptionHandler(TimeslotBusyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorDto timeslotBusyException(TimeslotBusyException e) {
        log.error("Timeslot is busy: {}", e.getMessage());
        return new ErrorDto(e.getMessage());
    }

    @ExceptionHandler(RequestValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorDto requestValidationException(RequestValidationException e) {
        log.error("Request validation error: {}", e.getMessage());
        return new ErrorDto(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ErrorDto commonHandler(Exception e) {
        log.error("Server error: {}", e.getMessage(), e);
        return new ErrorDto(e.getMessage());
    }
}