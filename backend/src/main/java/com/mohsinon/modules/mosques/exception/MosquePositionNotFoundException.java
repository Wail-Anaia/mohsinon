package com.mohsinon.modules.mosques.exception;

import com.mohsinon.common.exception.ResourceNotFoundException;

public class MosquePositionNotFoundException extends ResourceNotFoundException {

    public MosquePositionNotFoundException(Long id) {
        super("Mosque position with id '" + id + "' was not found.");
    }

    public MosquePositionNotFoundException(String code) {
        super("Mosque position with code '" + code + "' was not found.");
    }

}