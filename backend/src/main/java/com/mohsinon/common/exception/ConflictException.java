package com.mohsinon.common.exception;

import org.springframework.http.HttpStatus;

public abstract class ConflictException extends BusinessException {

    protected ConflictException(
            String errorCode,
            String message) {

        super(
                errorCode,
                message,
                HttpStatus.CONFLICT);
    }

}