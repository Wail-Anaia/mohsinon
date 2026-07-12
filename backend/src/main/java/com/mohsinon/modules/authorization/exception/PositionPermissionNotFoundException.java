package com.mohsinon.modules.authorization.exception;

import com.mohsinon.common.exception.ResourceNotFoundException;

public class PositionPermissionNotFoundException
        extends ResourceNotFoundException {

    public PositionPermissionNotFoundException() {
        super("Position permission was not found.");
    }

}