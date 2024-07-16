package com.shdv09.visitservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccessDeniedException extends RuntimeException {
    private final String message;
}
