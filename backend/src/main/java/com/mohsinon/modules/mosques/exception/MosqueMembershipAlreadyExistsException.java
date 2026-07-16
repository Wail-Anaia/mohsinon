package com.mohsinon.modules.mosques.exception;

import java.util.UUID;

import com.mohsinon.common.exception.AlreadyExistsException;
import com.mohsinon.common.exception.ErrorCodes;

public class MosqueMembershipAlreadyExistsException extends AlreadyExistsException {
	
	public MosqueMembershipAlreadyExistsException() {

        super(
                ErrorCodes.MOSQUE_MEMBERSHIP_ALREADY_EXISTS,
                "User already has an active membership in mosque.");
    }

    public MosqueMembershipAlreadyExistsException(UUID userId, UUID mosqueId) {

        super(
                ErrorCodes.MOSQUE_MEMBERSHIP_ALREADY_EXISTS,
                "User '" + userId + "' already has an active membership in mosque '" + mosqueId + "'.");
    }

}