package com.mohsinon.exception;

public class MosquePositionNotFoundException extends RuntimeException {

    public MosquePositionNotFoundException(String code) {
        super("Mosque position not found: " + code);
    }
}