package com.mohsinon.modules.mosques.exception;

import com.mohsinon.common.exception.AlreadyExistsException;
import com.mohsinon.common.exception.ErrorCodes;

public class MosquePositionAlreadyAssignedException extends AlreadyExistsException {

    public MosquePositionAlreadyAssignedException() {

        super(
                ErrorCodes.MOSQUE_POSITION_ALREADY_ASSIGNED,
                "The position is already assigned to another active member.");
    }
    
    public MosquePositionAlreadyAssignedException(String positionCode) {
        super(
                ErrorCodes.MOSQUE_POSITION_ALREADY_ASSIGNED,
                "Position '" + positionCode + "' is already assigned.");
    }

}