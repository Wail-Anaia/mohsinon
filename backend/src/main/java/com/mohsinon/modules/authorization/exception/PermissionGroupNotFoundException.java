package com.mohsinon.modules.authorization.exception;

import com.mohsinon.common.exception.ErrorCodes;
import com.mohsinon.common.exception.NotFoundException;

public class PermissionGroupNotFoundException extends NotFoundException {

    public PermissionGroupNotFoundException(Long id) {

        super(
                ErrorCodes.PERMISSION_GROUP_NOT_FOUND,
                "Permission group with id '" + id + "' was not found.");
    }

    public PermissionGroupNotFoundException(String code) {

        super(
                ErrorCodes.PERMISSION_GROUP_NOT_FOUND,
                "Permission group '" + code + "' was not found.");
    }

}