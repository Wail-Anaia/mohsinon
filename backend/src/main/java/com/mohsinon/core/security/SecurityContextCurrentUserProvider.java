package com.mohsinon.core.security;

import com.mohsinon.core.exception.UnauthorizedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class SecurityContextCurrentUserProvider implements CurrentUserProvider {

    @Override
    public Optional<UUID> getCurrentUserId() {
        return getCurrentUser().map(UserPrincipal::getId);
    }

    @Override
    public Optional<UserPrincipal> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserPrincipal userPrincipal) {
            return Optional.of(userPrincipal);
        }

        return Optional.empty();
    }

    @Override
    public UUID requireCurrentUserId() {
        return getCurrentUserId().orElseThrow(() -> new UnauthorizedException("AUTHENTICATION_REQUIRED", "Action requires an authenticated user."));
    }

    @Override
    public boolean isAuthenticated() {
        return getCurrentUser().isPresent();
    }
}
