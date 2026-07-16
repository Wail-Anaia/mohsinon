package com.mohsinon.modules.authorization.exception;

import com.mohsinon.common.exception.AlreadyExistsException;
import com.mohsinon.common.exception.ErrorCodes;

public class DuplicatePermissionException extends AlreadyExistsException {

    public DuplicatePermissionException(String code) {

        super(
                ErrorCodes.PERMISSION_ALREADY_EXISTS,
                "Permission '" + code + "' already exists.");
    }

}