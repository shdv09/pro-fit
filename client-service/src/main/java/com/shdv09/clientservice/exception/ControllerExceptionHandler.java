package com.shdv09.clientservice.exception;

import com.shdv09.clientservice.dto.response.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
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
    ResponseEntity<ErrorDto> notFoundException(NotFoundException e) {
        log.error("Object not found: {}", e.getMessage());
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.NOT_FOUND);
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

    @ExceptionHandler(AuthorizationDeniedException.class)
    ResponseEntity<ErrorDto> notFoundException(AuthorizationDeniedException e) {
        log.error("Access denied: {}", e.getMessage());
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorDto> commonHandler(Exception e) {
        log.error("Server error: {}", e.getMessage(), e);
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

    }
}