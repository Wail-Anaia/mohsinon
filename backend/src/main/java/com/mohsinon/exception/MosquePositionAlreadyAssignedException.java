package com.mohsinon.exception;

public class MosquePositionAlreadyAssignedException extends RuntimeException {

    public MosquePositionAlreadyAssignedException(String positionName) {
        super(positionName + " is already assigned in this mosque.");
    }
}