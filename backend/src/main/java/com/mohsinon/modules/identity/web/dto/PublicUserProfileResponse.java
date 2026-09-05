package com.mohsinon.modules.identity.web.dto;

import com.mohsinon.core.domain.GeoLocation;
import com.mohsinon.modules.identity.domain.User;
import com.mohsinon.modules.identity.domain.UserProfile;

import java.time.Instant;
import java.util.UUID;

/**
 * Public profile response accessible by community members.
 * Strictly enforces Privacy-by-Design by concealing private contacts,
 * exact residence GPS coordinates, and internal account status.
 */
public class PublicUserProfileResponse {

    private UUID id;
    private String username;
    private String displayName;
    private String bio;
    private String avatarUrl;
    private String preferredLanguage;
    private String city;
    private String countryCode;
    private GeoLocation approximateLocation;
    private Instant memberSince;

    public PublicUserProfileResponse() {
    }

    public PublicUserProfileResponse(UUID id, String username, String displayName, String bio,
                                     String avatarUrl, String preferredLanguage, String city,
                                     String countryCode, GeoLocation approximateLocation,
                                     Instant memberSince) {
        this.id = id;
        this.username = username;
        this.displayName = displayName;
        this.bio = bio;
        this.avatarUrl = avatarUrl;
        this.preferredLanguage = preferredLanguage;
        this.city = city;
        this.countryCode = countryCode;
        this.approximateLocation = approximateLocation;
        this.memberSince = memberSince;
    }

    public static PublicUserProfileResponse from(User user, UserProfile profile) {
        if (user == null) return null;

        String bio = profile != null ? profile.getBio() : null;
        String avatarUrl = profile != null ? profile.getAvatarUrl() : null;
        String preferredLanguage = profile != null ? profile.getPreferredLanguage() : UserProfile.DEFAULT_LANGUAGE;
        GeoLocation approximateLocation = profile != null ? profile.getApproximateLocation() : null;
        String city = approximateLocation != null ? approximateLocation.getCity() : null;
        String countryCode = approximateLocation != null ? approximateLocation.getCountryCode() : null;

        return new PublicUserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getDisplayName(),
                bio,
                avatarUrl,
                preferredLanguage,
                city,
                countryCode,
                approximateLocation,
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public GeoLocation getApproximateLocation() {
        return approximateLocation;
    }

    public void setApproximateLocation(GeoLocation approximateLocation) {
        this.approximateLocation = approximateLocation;
    }

    public Instant getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(Instant memberSince) {
        this.memberSince = memberSince;
    }
}
