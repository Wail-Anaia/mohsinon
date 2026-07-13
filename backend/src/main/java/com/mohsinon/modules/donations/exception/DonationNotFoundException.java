package com.mohsinon.modules.donations.exception;

import java.util.UUID;

public class DonationNotFoundException extends RuntimeException {

    public DonationNotFoundException(UUID id) {
        super("Donation not found with id: " + id);
    }

    public DonationNotFoundException() {
        super("Donation not found.");
    }

}