package com.mohsinon.common.exception;

import org.springframework.http.HttpStatus;

public abstract class ValidationException extends BusinessException {

    protected ValidationException(
            String errorCode,
            String message) {

        super(
                errorCode,
                message,
                HttpStatus.BAD_REQUEST);
    }

}