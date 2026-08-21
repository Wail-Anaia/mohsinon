package com.mohsinon.core.security.authorization.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Value Object encapsulating a bounded resource context (Type + Unique Identifier).
 * Used to evaluate contextual permissions against a specific protected resource instance.
 */
public record ResourceContext(String resourceType, UUID resourceId) implements Serializable {

    public static final String TYPE_MOSQUE = "MOSQUE";
    public static final String TYPE_PROJECT = "PROJECT";
    public static final String TYPE_INITIATIVE = "INITIATIVE";
    public static final String TYPE_DONATION = "DONATION";

    public ResourceContext {
        if (resourceType == null || resourceType.trim().isEmpty()) {
            throw new IllegalArgumentException("Resource type must not be null or empty.");
        }
        if (resourceId == null) {
            throw new IllegalArgumentException("Resource ID must not be null.");
        }
        resourceType = resourceType.trim().toUpperCase();
    }

    public static ResourceContext mosque(UUID mosqueId) {
        return new ResourceContext(TYPE_MOSQUE, mosqueId);
    }

    public static ResourceContext project(UUID projectId) {
        return new ResourceContext(TYPE_PROJECT, projectId);
    }

    public static ResourceContext initiative(UUID initiativeId) {
        return new ResourceContext(TYPE_INITIATIVE, initiativeId);
    }

    public static ResourceContext donation(UUID donationId) {
        return new ResourceContext(TYPE_DONATION, donationId);
    }

    public static ResourceContext of(String resourceType, UUID resourceId) {
        return new ResourceContext(resourceType, resourceId);
    }
}
