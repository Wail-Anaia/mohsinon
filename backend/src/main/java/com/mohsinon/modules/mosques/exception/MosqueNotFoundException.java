package com.mohsinon.modules.mosques.exception;

import com.mohsinon.common.exception.ResourceNotFoundException;

public class MosqueNotFoundException extends ResourceNotFoundException {

    public MosqueNotFoundException() {
        super("Mosque not found");
    }

    public MosqueNotFoundException(String message) {
        super(message);
    }

}