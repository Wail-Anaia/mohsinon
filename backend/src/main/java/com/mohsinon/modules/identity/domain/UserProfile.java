package com.mohsinon.modules.identity.domain;

import com.mohsinon.core.domain.BaseEntity;
import com.mohsinon.core.domain.GeoLocation;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.Locale;
import java.util.UUID;

/**
 * Domain entity representing an extended user profile and community persona.
 * Maintains 1:1 relationship with User identity while isolating profile preferences,
 * bio, avatar, and approximate location.
 */
@Entity
@Table(
        name = "user_profiles",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_profiles_user_id", columnNames = "user_id")
        },
        indexes = {
                @Index(name = "idx_user_profiles_user_id", columnList = "user_id"),
                @Index(name = "idx_user_profiles_city", columnList = "city")
        }
)
public class UserProfile extends BaseEntity {

    public static final int MAX_BIO_LENGTH = 500;
    public static final String DEFAULT_LANGUAGE = "fr";

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "bio", length = MAX_BIO_LENGTH)
    private String bio;

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @Column(name = "preferred_language", nullable = false, length = 10)
    private String preferredLanguage = DEFAULT_LANGUAGE;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "longitude")),
            @AttributeOverride(name = "city", column = @Column(name = "city", length = 100)),
            @AttributeOverride(name = "countryCode", column = @Column(name = "country_code", length = 3))
    })
    private GeoLocation location;

    public UserProfile() {
    }

    public UserProfile(UUID userId) {
        setUserId(userId);
        this.preferredLanguage = DEFAULT_LANGUAGE;
    }

    public UserProfile(UUID userId, String bio, String avatarUrl, String phoneNumber, String preferredLanguage, GeoLocation location) {
        setUserId(userId);
        setBio(bio);
        setAvatarUrl(avatarUrl);
        setPhoneNumber(phoneNumber);
        setPreferredLanguage(preferredLanguage);
        this.location = location;
    }

    public static UserProfile defaultFor(UUID userId) {
        return new UserProfile(userId);
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null.");
        }
        this.userId = userId;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        if (bio != null && bio.trim().length() > MAX_BIO_LENGTH) {
            throw new IllegalArgumentException("Bio must not exceed " + MAX_BIO_LENGTH + " characters.");
        }
        this.bio = bio != null ? bio.trim() : null;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl != null ? avatarUrl.trim() : null;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber != null ? phoneNumber.trim() : null;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        if (preferredLanguage != null && !preferredLanguage.trim().isEmpty()) {
            this.preferredLanguage = preferredLanguage.trim().toLowerCase(Locale.ROOT);
        } else {
            this.preferredLanguage = DEFAULT_LANGUAGE;
        }
    }

    public GeoLocation getLocation() {
        return location;
    }

    public void setLocation(GeoLocation location) {
        this.location = location;
    }

    /**
     * Helper to return approximate location ensuring privacy protection.
     */
    public GeoLocation getApproximateLocation() {
        return this.location != null ? this.location.toApproximate() : null;
    }
}
