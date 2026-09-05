package com.mohsinon.modules.identity.web.dto;

import com.mohsinon.core.domain.GeoLocation;
import com.mohsinon.modules.identity.domain.User;
import com.mohsinon.modules.identity.domain.UserProfile;
import com.mohsinon.modules.identity.domain.UserStatus;

import java.time.Instant;
import java.util.UUID;

/**
 * Complete Private profile response for the authenticated account owner.
 * Contains personal details, contact info, and preferences.
 */
public class PrivateUserProfileResponse {

    private UUID id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String displayName;
    private UserStatus status;
    private String bio;
    private String avatarUrl;
    private String phoneNumber;
    private String preferredLanguage;
    private String city;
    private String countryCode;
    private GeoLocation location;
    private Instant createdAt;

    public PrivateUserProfileResponse() {
    }

    public PrivateUserProfileResponse(UUID id, String username, String email, String firstName, String lastName,
                                      String displayName, UserStatus status, String bio, String avatarUrl,
                                      String phoneNumber, String preferredLanguage, String city,
                                      String countryCode, GeoLocation location, Instant createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName;
        this.status = status;
        this.bio = bio;
        this.avatarUrl = avatarUrl;
        this.phoneNumber = phoneNumber;
        this.preferredLanguage = preferredLanguage;
        this.city = city;
        this.countryCode = countryCode;
        this.location = location;
        this.createdAt = createdAt;
    }

    public static PrivateUserProfileResponse from(User user, UserProfile profile) {
        if (user == null) return null;

        String bio = profile != null ? profile.getBio() : null;
        String avatarUrl = profile != null ? profile.getAvatarUrl() : null;
        String phoneNumber = profile != null ? profile.getPhoneNumber() : null;
        String preferredLanguage = profile != null ? profile.getPreferredLanguage() : UserProfile.DEFAULT_LANGUAGE;
        GeoLocation location = profile != null ? profile.getLocation() : null;
        String city = location != null ? location.getCity() : null;
        String countryCode = location != null ? location.getCountryCode() : null;

        return new PrivateUserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getDisplayName(),
                user.getStatus(),
                bio,
                avatarUrl,
                phoneNumber,
                preferredLanguage,
                city,
                countryCode,
                location,
                user.getCreatedAt()
        );
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public GeoLocation getLocation() {
        return location;
    }

    public void setLocation(GeoLocation location) {
        this.location = location;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
