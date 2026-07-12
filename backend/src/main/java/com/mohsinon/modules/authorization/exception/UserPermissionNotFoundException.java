package com.mohsinon.modules.authorization.exception;

import java.util.UUID;

import com.mohsinon.common.exception.ResourceNotFoundException;

public class UserPermissionNotFoundException
extends ResourceNotFoundException {

	public UserPermissionNotFoundException() {
		super("User permission was not found.");
	}
	
	public UserPermissionNotFoundException(
	    UUID userId,
	    Long permissionId) {
	
		super("Permission '" + permissionId
	        + "' is not assigned to user '" + userId + "'.");
	}

}