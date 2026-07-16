package com.mohsinon.modules.authorization.exception;

import com.mohsinon.common.exception.ErrorCodes;
import com.mohsinon.common.exception.NotFoundException;

public class PermissionNotFoundException extends NotFoundException {

    public PermissionNotFoundException(Long id) {

        super(
                ErrorCodes.PERMISSION_NOT_FOUND,
                "Permission with id '" + id + "' was not found.");
    }
    
    public PermissionNotFoundException(String code) {

        super(
                ErrorCodes.PERMISSION_NOT_FOUND,
                "Permission with code '" + code + "' was not found.");
    }

}