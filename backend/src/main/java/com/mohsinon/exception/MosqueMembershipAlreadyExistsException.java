package com.mohsinon.exception;

public class MosqueMembershipAlreadyExistsException extends RuntimeException {

    public MosqueMembershipAlreadyExistsException() {
        super("User already has an active membership in this mosque.");
    }
}