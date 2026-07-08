package com.mohsinon.exception;

public class MosqueNotFoundException extends ResourceNotFoundException {

    public MosqueNotFoundException() {
        super("Mosque not found");
    }

    public MosqueNotFoundException(String message) {
        super(message);
    }

}