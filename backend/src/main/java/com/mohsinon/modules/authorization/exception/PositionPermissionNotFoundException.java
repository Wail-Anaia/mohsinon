package com.mohsinon.modules.authorization.exception;

import com.mohsinon.common.exception.ErrorCodes;
import com.mohsinon.common.exception.NotFoundException;

public class PositionPermissionNotFoundException extends NotFoundException {

    public PositionPermissionNotFoundException() {

        super(
                ErrorCodes.POSITION_PERMISSION_NOT_FOUND,
                "Position permission was not found.");
    }

    public PositionPermissionNotFoundException(Long positionId, Long permissionId) {

        super(
                ErrorCodes.POSITION_PERMISSION_NOT_FOUND,
                "Permission '" + permissionId
                        + "' is not assigned to position '" + positionId + "'.");
    }

}