package com.mohsinon.exception;

public class MosqueMembershipNotFoundException
        extends ResourceNotFoundException {

    public MosqueMembershipNotFoundException() {
        super("Mosque membership not found.");
    }

}