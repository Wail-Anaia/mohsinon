package com.mohsinon.core.security.authorization.service;

import com.mohsinon.core.security.authorization.model.PermissionType;
import com.mohsinon.core.security.authorization.model.ResourceContext;

import java.util.UUID;

/**
 * Central authorization engine contract for the Mohsinon platform.
 * Provides unified, deny-by-default evaluation across global and contextual dimensions.
 */
public interface AuthorizationService {

    /**
     * Evaluates whether a user has a permission globally across the entire platform.
     */
    boolean hasGlobalPermission(UUID userId, PermissionType permission);

    /**
     * Evaluates whether a user has a permission within a specific bounded resource context.
     */
    boolean hasPermission(UUID userId, PermissionType permission, ResourceContext context);

    /**
     * Convenience method to evaluate permission on a resource by type and ID.
     */
    boolean hasPermission(UUID userId, PermissionType permission, String resourceType, UUID resourceId);

    /**
     * Convenience check specifically for mosque management operations.
     */
    boolean canManageMosque(UUID userId, UUID mosqueId);

    /**
     * Checks if the user is a platform administrator.
     */
    boolean isAdmin(UUID userId);

    /**
     * Asserts that a user has a global permission; throws ForbiddenException if denied.
     */
    void requireGlobalPermission(UUID userId, PermissionType permission);

    /**
     * Asserts that a user has a permission in context; throws ForbiddenException if denied.
     */
    void requirePermission(UUID userId, PermissionType permission, ResourceContext context);

    /**
     * Asserts that a user has a permission on a resource; throws ForbiddenException if denied.
     */
    void requirePermission(UUID userId, PermissionType permission, String resourceType, UUID resourceId);
}
