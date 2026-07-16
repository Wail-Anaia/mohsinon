package com.mohsinon.modules.mosques.exception;

import java.util.UUID;

import com.mohsinon.common.exception.ErrorCodes;
import com.mohsinon.common.exception.NotFoundException;

public class MosqueMembershipNotFoundException extends NotFoundException {
	
	public MosqueMembershipNotFoundException() {
        super(
                ErrorCodes.MOSQUE_MEMBERSHIP_NOT_FOUND,
                "Mosque membership not found.");
    }

    public MosqueMembershipNotFoundException(UUID id) {

        super(
                ErrorCodes.MOSQUE_MEMBERSHIP_NOT_FOUND,
                "Mosque membership with id '" + id + "' was not found.");
    }

}