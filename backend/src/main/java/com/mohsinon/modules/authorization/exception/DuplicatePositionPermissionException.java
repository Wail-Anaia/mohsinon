package com.mohsinon.modules.authorization.exception;

import com.mohsinon.common.exception.DuplicateResourceException;

public class DuplicatePositionPermissionException
        extends DuplicateResourceException {

    public DuplicatePositionPermissionException() {
        super("Permission is already assigned to this position.");
    }

}