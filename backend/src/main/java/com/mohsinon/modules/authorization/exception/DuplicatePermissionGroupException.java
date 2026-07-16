package com.mohsinon.modules.authorization.exception;

import com.mohsinon.common.exception.AlreadyExistsException;
import com.mohsinon.common.exception.ErrorCodes;

public class DuplicatePermissionGroupException extends AlreadyExistsException {

    public DuplicatePermissionGroupException(String code) {

        super(
                ErrorCodes.PERMISSION_GROUP_ALREADY_EXISTS,
                "Permission group '" + code + "' already exists.");
    }

}