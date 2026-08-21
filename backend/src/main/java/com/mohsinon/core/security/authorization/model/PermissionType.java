package com.mohsinon.core.security.authorization.model;

/**
 * Granular, strongly-typed business capabilities of the Mohsinon platform.
 * Represents specific actions that can be authorized globally or contextually.
 */
public enum PermissionType {
    // Mosque management capabilities
    MOSQUE_VIEW,
    MOSQUE_UPDATE,
    MOSQUE_MANAGE_MEMBERS,
    MOSQUE_VERIFY,

    // Donation capabilities
    DONATION_CREATE,
    DONATION_MANAGE,
    DONATION_VERIFY,

    // Initiative & Project capabilities
    INITIATIVE_CREATE,
    INITIATIVE_APPROVE,
    PROJECT_MANAGE,

    // Volunteer capabilities
    VOLUNTEER_MANAGE,

    // Impact / Evidence verification capability
    IMPACT_VERIFY,

    // Administrative super-capability
    ADMIN_ALL
}
