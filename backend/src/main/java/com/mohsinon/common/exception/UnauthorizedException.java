package com.mohsinon.common.exception;

import org.springframework.http.HttpStatus;

public abstract class UnauthorizedException extends BusinessException {

    protected UnauthorizedException(
            String errorCode,
            String message) {

        super(
                errorCode,
                message,
                HttpStatus.UNAUTHORIZED);
    }

}