package com.shdv09.webapplication.exception;

import com.shdv09.webapplication.dto.response.ErrorDto;
import feign.FeignException;
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
import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(FeignException.class)
    ResponseEntity<ErrorDto> feignException(FeignException e) {
        log.error("Feign client exception: {}", e.getMessage());
        HttpStatus httpStatus = Optional.ofNullable(HttpStatus.resolve(e.status()))
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(
                new ErrorDto(e.getMessage()), httpStatus);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    ResponseEntity<ErrorDto> notFoundException(AuthorizationDeniedException e) {
        log.error("Access denied: {}", e.getMessage());
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.FORBIDDEN);
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
    ResponseEntity<ErrorDto> commonHandler(Exception e) {
        log.error("Server error: {}", e.getMessage(), e);
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

    }
}