package com.mohsinon.modules.authorization.exception;

import com.mohsinon.common.exception.ResourceNotFoundException;

public class PermissionNotFoundException extends ResourceNotFoundException {

    public PermissionNotFoundException(Long id) {
        super("Permission with id '" + id + "' was not found.");
    }

    public PermissionNotFoundException(String code) {
        super("Permission with code '" + code + "' was not found.");
    }

}