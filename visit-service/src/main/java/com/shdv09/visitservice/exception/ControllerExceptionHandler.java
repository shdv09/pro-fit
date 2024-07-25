package com.shdv09.visitservice.exception;

import com.shdv09.visitservice.dto.response.ErrorDto;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ErrorDto> accessDeniedException(AccessDeniedException e) {
        log.error("Access to club denied: {}", e.getMessage());
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(FeignException.class)
    ResponseEntity<ErrorDto> feignException(FeignException e) {
        log.error("Feign client exception: {}", e.getMessage());
        HttpStatus httpStatus = Optional.ofNullable(HttpStatus.resolve(e.status()))
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(
                new ErrorDto(e.getMessage()), httpStatus);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorDto requestValidationException(MethodArgumentNotValidException e) {
        log.error("Request validation error: {}", e.getBindingResult().getTarget());
        return new ErrorDto("Validation failed for: %s".formatted(e.getBindingResult().getTarget()));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorDto> commonHandler(Exception e) {
        log.error("Server error: {}", e.getMessage(), e);
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

    }
}