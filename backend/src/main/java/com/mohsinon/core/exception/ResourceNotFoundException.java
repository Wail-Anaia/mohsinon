package com.mohsinon.core.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String resourceName, UUID id) {
        super("RESOURCE_NOT_FOUND", String.format("%s with id '%s' was not found.", resourceName, id), HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(String resourceName, String identifierName, String identifierValue) {
        super("RESOURCE_NOT_FOUND", String.format("%s with %s '%s' was not found.", resourceName, identifierName, identifierValue), HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(String message) {
        super("RESOURCE_NOT_FOUND", message, HttpStatus.NOT_FOUND);
    }
}
