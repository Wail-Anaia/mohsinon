package com.mohsinon.common.exception;

import org.springframework.http.HttpStatus;

public abstract class AlreadyExistsException extends BusinessException {

    protected AlreadyExistsException(
            String errorCode,
            String message) {

        super(
                errorCode,
                message,
                HttpStatus.CONFLICT);
    }

}