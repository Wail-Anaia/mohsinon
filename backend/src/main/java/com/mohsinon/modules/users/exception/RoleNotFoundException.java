package com.mohsinon.modules.users.exception;

import com.mohsinon.common.exception.ErrorCodes;
import com.mohsinon.common.exception.NotFoundException;

public class RoleNotFoundException extends NotFoundException {

    public RoleNotFoundException(String roleName) {

        super(
                ErrorCodes.ROLE_NOT_FOUND,
                "Role '" + roleName + "' was not found.");
    }

}