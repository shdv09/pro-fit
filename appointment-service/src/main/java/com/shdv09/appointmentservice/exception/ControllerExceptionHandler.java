package com.shdv09.appointmentservice.exception;

import com.shdv09.appointmentservice.dto.response.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorDto requestValidationException(MethodArgumentNotValidException e) {
        List<String> errorFields = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getField)
                .sorted()
                .toList();
        String errorFieldList = String.join(", ", errorFields);
        log.error("Request validation error: {}", errorFieldList);
        return new ErrorDto("Validation failed for: %s".formatted(errorFieldList));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ErrorDto commonHandler(Exception e) {
        log.error("Server error: {}", e.getMessage(), e);
        return new ErrorDto(e.getMessage());
    }
}