package com.mohsinon.modules.authorization.exception;

import com.mohsinon.common.exception.AlreadyExistsException;
import com.mohsinon.common.exception.ErrorCodes;

public class DuplicatePositionPermissionException extends AlreadyExistsException {
	
	public DuplicatePositionPermissionException() {

        super(
                ErrorCodes.POSITION_PERMISSION_ALREADY_EXISTS,
                "Permission is already assigned to position.");
    }

    public DuplicatePositionPermissionException(
            Long positionId,
            Long permissionId) {

        super(
                ErrorCodes.POSITION_PERMISSION_ALREADY_EXISTS,
                "Permission '" + permissionId
                        + "' is already assigned to position '"
                        + positionId + "'.");
    }

}