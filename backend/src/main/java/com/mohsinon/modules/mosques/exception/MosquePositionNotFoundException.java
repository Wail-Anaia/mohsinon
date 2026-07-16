package com.mohsinon.modules.mosques.exception;

import com.mohsinon.common.exception.ErrorCodes;
import com.mohsinon.common.exception.NotFoundException;

public class MosquePositionNotFoundException extends NotFoundException {

    public MosquePositionNotFoundException(String code) {

        super(
                ErrorCodes.MOSQUE_POSITION_NOT_FOUND,
                "Mosque position '" + code + "' was not found.");
    }
    
    public MosquePositionNotFoundException(Long id) {

        super(
                ErrorCodes.MOSQUE_POSITION_NOT_FOUND,
                "Mosque position with id '" + id + "' was not found.");
    }

}