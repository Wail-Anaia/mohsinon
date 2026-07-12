package com.mohsinon.modules.authorization.exception;

import com.mohsinon.common.exception.DuplicateResourceException;

public class DuplicateUserPermissionException
        extends DuplicateResourceException {

    public DuplicateUserPermissionException() {
        super("Permission is already assigned to this user.");
    }

}