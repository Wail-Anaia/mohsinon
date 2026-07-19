package com.mohsinon.common.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends BusinessException {

    public AuthenticationException(String message) {
        super(
        	ErrorCodes.AUTHENTICATION_FAILED,
            message,
            HttpStatus.UNAUTHORIZED
            
        );
    }

}