package com.mohsinon.modules.authorization.exception;

import java.util.UUID;

import com.mohsinon.common.exception.AlreadyExistsException;
import com.mohsinon.common.exception.ErrorCodes;

public class DuplicateUserPermissionException extends AlreadyExistsException {
	
	public DuplicateUserPermissionException() {

        super(
                ErrorCodes.USER_PERMISSION_ALREADY_EXISTS,
                "Permission is already assigned to user.");
    }

    public DuplicateUserPermissionException(
            UUID userId,
            Long permissionId) {

        super(
                ErrorCodes.USER_PERMISSION_ALREADY_EXISTS,
                "Permission '" + permissionId
                        + "' is already assigned to user '"
                        + userId + "'.");
    }

}