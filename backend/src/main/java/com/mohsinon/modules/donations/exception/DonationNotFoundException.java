package com.mohsinon.modules.donations.exception;

import java.util.UUID;

import com.mohsinon.common.exception.NotFoundException;
import static com.mohsinon.common.exception.ErrorCodes.DONATION_NOT_FOUND;

public class DonationNotFoundException extends NotFoundException {

    public DonationNotFoundException(UUID id) {
        super(DONATION_NOT_FOUND,
            "Donation with id '%s' was not found.".formatted(id)
        );
    }

}