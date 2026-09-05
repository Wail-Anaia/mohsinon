package com.mohsinon.modules.identity.service;

import com.mohsinon.modules.identity.domain.UserProfile;
import com.mohsinon.modules.identity.web.dto.PrivateUserProfileResponse;
import com.mohsinon.modules.identity.web.dto.PublicUserProfileResponse;
import com.mohsinon.modules.identity.web.dto.UpdateUserProfileRequest;

import java.util.UUID;

public interface UserProfileService {

    /**
     * Retrieves the complete private profile for the authenticated user.
     */
    PrivateUserProfileResponse getMyProfile(UUID userId);

    /**
     * Retrieves the public profile for any user by their username.
     */
    PublicUserProfileResponse getPublicProfileByUsername(String username);

    /**
     * Retrieves the public profile for any user by their unique user ID.
     */
    PublicUserProfileResponse getPublicProfileById(UUID userId);

    /**
     * Updates the private profile of the authenticated user.
     */
    PrivateUserProfileResponse updateMyProfile(UUID userId, UpdateUserProfileRequest request);

    /**
     * Retrieves or lazily creates a default UserProfile entity for a given user.
     */
    UserProfile getOrCreateProfile(UUID userId);
}
