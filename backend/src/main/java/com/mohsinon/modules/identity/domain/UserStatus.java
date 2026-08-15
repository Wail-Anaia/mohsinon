package com.mohsinon.modules.identity.domain;

/**
 * Status of a user account in Mohsinon.
 */
public enum UserStatus {
    ACTIVE,
    INACTIVE,
    SUSPENDED,
    PENDING_VERIFICATION;

    public boolean canAuthenticate() {
        return this == ACTIVE;
    }
}
