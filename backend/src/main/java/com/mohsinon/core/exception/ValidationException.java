package com.mohsinon.core.exception;

import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

public class ValidationException extends BusinessException {

    private final Map<String, String> errors;

    public ValidationException(String message) {
        super("VALIDATION_FAILED", message, HttpStatus.BAD_REQUEST);
        this.errors = Collections.emptyMap();
    }

    public ValidationException(String message, Map<String, String> errors) {
        super("VALIDATION_FAILED", message, HttpStatus.BAD_REQUEST);
        this.errors = errors != null ? errors : Collections.emptyMap();
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
