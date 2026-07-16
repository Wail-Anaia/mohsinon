package com.mohsinon.modules.users.exception;

import java.util.UUID;

import com.mohsinon.common.exception.ErrorCodes;
import com.mohsinon.common.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
	
	public UserNotFoundException() {
        super(
                ErrorCodes.USER_NOT_FOUND,
                "User not found.");
    }

    public UserNotFoundException(UUID id) {

        super(
                ErrorCodes.USER_NOT_FOUND,
                "User with id '" + id + "' was not found.");
    }

    public UserNotFoundException(String username) {

        super(
                ErrorCodes.USER_NOT_FOUND,
                "User '" + username + "' was not found.");
    }

    public UserNotFoundException(String field, String value) {

        super(
                ErrorCodes.USER_NOT_FOUND,
                "User with " + field + " '" + value + "' was not found.");
    }

}