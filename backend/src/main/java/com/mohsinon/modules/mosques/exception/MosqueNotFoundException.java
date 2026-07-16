package com.mohsinon.modules.mosques.exception;

import java.util.UUID;

import com.mohsinon.common.exception.NotFoundException;
import com.mohsinon.common.exception.ErrorCodes;

public class MosqueNotFoundException extends NotFoundException {
	
	public MosqueNotFoundException() {
        super(
                ErrorCodes.MOSQUE_NOT_FOUND,
                "Mosque not found.");
    }
	
    public MosqueNotFoundException(UUID id) {

        super(
                ErrorCodes.MOSQUE_NOT_FOUND,
                "Mosque with id '" + id + "' was not found.");
    }

}