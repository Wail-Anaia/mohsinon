package com.mohsinon.modules.authorization.exception;

import com.mohsinon.common.exception.DuplicateResourceException;

public class DuplicatePermissionGroupException
        extends DuplicateResourceException {

    public DuplicatePermissionGroupException(String code) {
        super("Permission Group with code '" + code + "' already exists.");
    }

}