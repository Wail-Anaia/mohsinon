package com.mohsinon.core.security;

import java.util.Optional;
import java.util.UUID;

/**
 * Clean abstraction decoupling business services from SecurityContextHolder.
 */
public interface CurrentUserProvider {

    Optional<UUID> getCurrentUserId();

    Optional<UserPrincipal> getCurrentUser();

    UUID requireCurrentUserId();

    boolean isAuthenticated();
}
