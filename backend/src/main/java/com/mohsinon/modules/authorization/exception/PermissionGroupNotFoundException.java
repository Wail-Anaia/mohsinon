package com.mohsinon.modules.authorization.exception;

import com.mohsinon.common.exception.ResourceNotFoundException;

public class PermissionGroupNotFoundException
        extends ResourceNotFoundException {

    public PermissionGroupNotFoundException(Long id) {
        super("Permission Group with id " + id + " was not found.");
    }

}