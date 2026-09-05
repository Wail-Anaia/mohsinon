package com.mohsinon.modules.identity.service;

import com.mohsinon.core.domain.GeoLocation;
import com.mohsinon.core.exception.ResourceNotFoundException;
import com.mohsinon.modules.identity.domain.User;
import com.mohsinon.modules.identity.domain.UserProfile;
import com.mohsinon.modules.identity.domain.UserStatus;
import com.mohsinon.modules.identity.repository.UserProfileRepository;
import com.mohsinon.modules.identity.repository.UserRepository;
import com.mohsinon.modules.identity.web.dto.PrivateUserProfileResponse;
import com.mohsinon.modules.identity.web.dto.PublicUserProfileResponse;
import com.mohsinon.modules.identity.web.dto.UpdateUserProfileRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.UUID;

@Service
@Transactional
public class DefaultUserProfileService implements UserProfileService {

    private static final Logger log = LoggerFactory.getLogger(DefaultUserProfileService.class);

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    public DefaultUserProfileService(UserRepository userRepository, UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PrivateUserProfileResponse getMyProfile(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseGet(() -> UserProfile.defaultFor(userId));

        return PrivateUserProfileResponse.from(user, profile);
    }

    @Override
    @Transactional(readOnly = true)
    public PublicUserProfileResponse getPublicProfileByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username must not be null or empty.");
        }

        String normalizedUsername = username.trim().toLowerCase(Locale.ROOT);
        User user = userRepository.findByUsername(normalizedUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        if (user.getStatus() != UserStatus.ACTIVE) {
            log.warn("Attempt to view public profile of inactive user: {}", username);
            throw new ResourceNotFoundException("User", "username", username);
        }

        UserProfile profile = userProfileRepository.findByUserId(user.getId())
                .orElseGet(() -> UserProfile.defaultFor(user.getId()));

        return PublicUserProfileResponse.from(user, profile);
    }

    @Override
    @Transactional(readOnly = true)
    public PublicUserProfileResponse getPublicProfileById(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        if (user.getStatus() != UserStatus.ACTIVE) {
            log.warn("Attempt to view public profile of inactive user id: {}", userId);
            throw new ResourceNotFoundException("User", userId);
        }

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseGet(() -> UserProfile.defaultFor(userId));

        return PublicUserProfileResponse.from(user, profile);
    }

    @Override
    public PrivateUserProfileResponse updateMyProfile(UUID userId, UpdateUserProfileRequest request) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null.");
        }
        if (request == null) {
            throw new IllegalArgumentException("Update request must not be null.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        // 1. Update User names if provided
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getDisplayName() != null) {
            user.setDisplayName(request.getDisplayName());
        }
        userRepository.save(user);

        // 2. Update or create UserProfile
        UserProfile profile = getOrCreateProfile(userId);

        if (request.getBio() != null) {
            profile.setBio(request.getBio());
        }
        if (request.getAvatarUrl() != null) {
            profile.setAvatarUrl(request.getAvatarUrl());
        }
        if (request.getPhoneNumber() != null) {
            profile.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getPreferredLanguage() != null) {
            profile.setPreferredLanguage(request.getPreferredLanguage());
        }

        // 3. Update Location if city/country/coordinates provided
        GeoLocation currentLocation = profile.getLocation();
        String city = request.getCity() != null ? request.getCity() : (currentLocation != null ? currentLocation.getCity() : null);
        String countryCode = request.getCountryCode() != null ? request.getCountryCode() : (currentLocation != null ? currentLocation.getCountryCode() : null);
        Double latitude = request.getLatitude() != null ? request.getLatitude() : (currentLocation != null ? currentLocation.getLatitude() : null);
        Double longitude = request.getLongitude() != null ? request.getLongitude() : (currentLocation != null ? currentLocation.getLongitude() : null);

        if (city != null || countryCode != null || latitude != null || longitude != null) {
            profile.setLocation(new GeoLocation(latitude, longitude, city, countryCode));
        }

        UserProfile savedProfile = userProfileRepository.save(profile);
        log.info("Updated profile for user: {}", userId);

        return PrivateUserProfileResponse.from(user, savedProfile);
    }

    @Override
    public UserProfile getOrCreateProfile(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null.");
        }
        return userProfileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    UserProfile newProfile = UserProfile.defaultFor(userId);
                    return userProfileRepository.save(newProfile);
                });
    }
}
