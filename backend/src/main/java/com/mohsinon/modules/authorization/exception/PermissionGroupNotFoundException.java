package com.mohsinon.modules.authorization.exception;

import com.mohsinon.common.exception.ResourceNotFoundException;

public class PermissionGroupNotFoundException
        extends ResourceNotFoundException {
	
	public PermissionGroupNotFoundException() {
        super("Permission Group was not found.");
    } 
	
    public PermissionGroupNotFoundException(Long id) {
        super("Permission Group with id " + id + " was not found.");
    }

}