package com.mohsinon.modules.authorization.exception;

import java.util.UUID;

import com.mohsinon.common.exception.ErrorCodes;
import com.mohsinon.common.exception.NotFoundException;

public class UserPermissionNotFoundException extends NotFoundException {

    public UserPermissionNotFoundException(UUID userId, Long permissionId) {

        super(
                ErrorCodes.USER_PERMISSION_NOT_FOUND,
                "Permission '" + permissionId
                        + "' was not assigned to user '" + userId + "'.");
    }
    
    public UserPermissionNotFoundException() {

        super(
                ErrorCodes.USER_PERMISSION_NOT_FOUND,
                "User permission was not found.");
    }

}