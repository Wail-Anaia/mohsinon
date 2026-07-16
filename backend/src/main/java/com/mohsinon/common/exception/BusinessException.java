package com.mohsinon.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {

    private final String errorCode;

    private final HttpStatus status;

    protected BusinessException(
            String errorCode,
            String message,
            HttpStatus status) {

        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

}