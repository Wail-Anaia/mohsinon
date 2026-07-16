package com.mohsinon.modules.users.exception;

import com.mohsinon.common.exception.AlreadyExistsException;
import com.mohsinon.common.exception.ErrorCodes;

public class UserAlreadyExistsException extends AlreadyExistsException {

    public UserAlreadyExistsException(String field, String value) {

        super(
                ErrorCodes.USER_ALREADY_EXISTS,
                "User with " + field + " '" + value + "' already exists.");
    }

}