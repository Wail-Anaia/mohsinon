package com.mohsinon.security.current;

import com.mohsinon.modules.users.entity.User;
import com.mohsinon.modules.users.repository.UserRepository;
import com.mohsinon.modules.users.exception.UserNotFoundException;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Returns the authenticated User entity.
     */
    public User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {

            throw new UserNotFoundException();
        }

        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    /**
     * Returns current user id.
     */
    public UUID getCurrentUserId() {
        return getCurrentUser().getId();
    }

    /**
     * Returns current username.
     */
    public String getUsername() {
        return getCurrentUser().getUsername();
    }

    /**
     * Returns current email.
     */
    public String getEmail() {
        return getCurrentUser().getEmail();
    }

    /**
     * Checks if a user is authenticated.
     */
    public boolean isAuthenticated() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }
}