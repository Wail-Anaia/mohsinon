package com.mohsinon.common.exception;

import org.springframework.http.HttpStatus;

public abstract class ForbiddenException extends BusinessException {

    protected ForbiddenException(
            String errorCode,
            String message) {

        super(
                errorCode,
                message,
                HttpStatus.FORBIDDEN);
    }

}