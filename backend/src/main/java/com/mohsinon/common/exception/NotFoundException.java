package com.mohsinon.common.exception;

import org.springframework.http.HttpStatus;

public abstract class NotFoundException extends BusinessException {

    protected NotFoundException(
            String errorCode,
            String message) {

        super(
                errorCode,
                message,
                HttpStatus.NOT_FOUND);
    }

}