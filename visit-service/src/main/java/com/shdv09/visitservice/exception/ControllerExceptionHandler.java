package com.shdv09.visitservice.exception;

import com.shdv09.visitservice.dto.response.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ErrorDto> accessDeniedException(AccessDeniedException e) {
        log.error("Access to club denied: {}", e.getMessage());
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorDto> commonHandler(Exception e) {
        log.error("Server error: {}", e.getMessage(), e);
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

    }
}