package com.mohsinon.modules.donations.exception;

public class DonationCategoryAlreadyExistsException extends RuntimeException {

    public DonationCategoryAlreadyExistsException(String code) {
        super("Donation category already exists with code: " + code);
    }

    public DonationCategoryAlreadyExistsException() {
        super("Donation category already exists.");
    }
}