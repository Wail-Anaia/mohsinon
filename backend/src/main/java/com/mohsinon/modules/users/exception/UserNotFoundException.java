package com.mohsinon.modules.users.exception;

import java.util.UUID;

import com.mohsinon.common.exception.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException() {
        super("User not found.");
    }
    
    public UserNotFoundException(UUID id) {
        super("User with id '" + id + "' was not found.");
    }

    public UserNotFoundException(String message) {
        super(message);
    }

}