package com.mohsinon.modules.authorization.exception;

import com.mohsinon.common.exception.DuplicateResourceException;

public class DuplicatePermissionException extends DuplicateResourceException {

    public DuplicatePermissionException(String code) {
        super("Permission with code '" + code + "' already exists.");
    }

}