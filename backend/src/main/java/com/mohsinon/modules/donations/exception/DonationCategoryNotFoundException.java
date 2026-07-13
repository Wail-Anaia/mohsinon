package com.mohsinon.modules.donations.exception;

import java.util.UUID;

public class DonationCategoryNotFoundException extends RuntimeException {

    public DonationCategoryNotFoundException(UUID id) {
        super("Donation category not found with id: " + id);
    }

    public DonationCategoryNotFoundException(String code) {
        super("Donation category not found with code: " + code);
    }

    public DonationCategoryNotFoundException() {
        super("Donation category not found.");
    }

}