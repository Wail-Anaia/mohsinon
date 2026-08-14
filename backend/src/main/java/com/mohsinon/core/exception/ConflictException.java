package com.mohsinon.core.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends BusinessException {

    public ConflictException(String message) {
        super("RESOURCE_CONFLICT", message, HttpStatus.CONFLICT);
    }

    public ConflictException(String errorCode, String message) {
        super(errorCode, message, HttpStatus.CONFLICT);
    }
}
