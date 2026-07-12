package com.mohsinon.modules.mosques.exception;

import com.mohsinon.common.exception.ResourceNotFoundException;

public class MosqueMembershipNotFoundException
        extends ResourceNotFoundException {

    public MosqueMembershipNotFoundException() {
        super("Mosque membership not found.");
    }

}