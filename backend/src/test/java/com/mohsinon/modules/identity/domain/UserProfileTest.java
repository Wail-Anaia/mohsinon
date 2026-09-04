package com.mohsinon.modules.identity.domain;

import com.mohsinon.core.domain.GeoLocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserProfileTest {

    @Test
    @DisplayName("Should create default user profile with French as default language")
    void defaultProfileCreation() {
        UUID userId = UUID.randomUUID();
        UserProfile profile = UserProfile.defaultFor(userId);

        assertThat(profile.getUserId()).isEqualTo(userId);
        assertThat(profile.getPreferredLanguage()).isEqualTo("fr");
        assertThat(profile.getBio()).isNull();
        assertThat(profile.getAvatarUrl()).isNull();
        assertThat(profile.getPhoneNumber()).isNull();
        assertThat(profile.getLocation()).isNull();
    }

    @Test
    @DisplayName("Should create profile with full parameters and normalize language and bio")
    void fullProfileCreation() {
        UUID userId = UUID.randomUUID();
        GeoLocation location = GeoLocation.of(48.856614, 2.3522219, "Paris", "FR");

        UserProfile profile = new UserProfile(
                userId,
                "  Passionné d'entraide communautaire.  ",
                "https://cdn.mohsinon.org/avatars/u1.png",
                "+33612345678",
                "  AR  ",
                location
        );

        assertThat(profile.getUserId()).isEqualTo(userId);
        assertThat(profile.getBio()).isEqualTo("Passionné d'entraide communautaire.");
        assertThat(profile.getAvatarUrl()).isEqualTo("https://cdn.mohsinon.org/avatars/u1.png");
        assertThat(profile.getPhoneNumber()).isEqualTo("+33612345678");
        assertThat(profile.getPreferredLanguage()).isEqualTo("ar");
        assertThat(profile.getLocation()).isEqualTo(location);
    }

    @Test
    @DisplayName("Should enforce bio character limit (max 500 characters)")
    void bioLengthValidation() {
        UUID userId = UUID.randomUUID();
        UserProfile profile = new UserProfile(userId);

        String validBio = "A".repeat(500);
        profile.setBio(validBio);
        assertThat(profile.getBio()).hasSize(500);

        String tooLongBio = "A".repeat(501);
        assertThatThrownBy(() -> profile.setBio(tooLongBio))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Bio must not exceed 500 characters");
    }

    @Test
    @DisplayName("Should reject null userId")
    void rejectNullUserId() {
        assertThatThrownBy(() -> new UserProfile(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User ID must not be null");

        UserProfile profile = new UserProfile(UUID.randomUUID());
        assertThatThrownBy(() -> profile.setUserId(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User ID must not be null");
    }

    @Test
    @DisplayName("Should provide approximate location for privacy preservation")
    void privacyPreservingLocation() {
        UUID userId = UUID.randomUUID();
        // Exact coordinates with micro-precision
        GeoLocation exactLocation = GeoLocation.of(48.856614, 2.3522219, "Paris", "FR");

        UserProfile profile = new UserProfile(userId, null, null, null, "fr", exactLocation);
        GeoLocation approximate = profile.getApproximateLocation();

        assertThat(approximate).isNotNull();
        assertThat(approximate.getLatitude()).isEqualTo(48.86);
        assertThat(approximate.getLongitude()).isEqualTo(2.35);
        assertThat(approximate.getCity()).isEqualTo("Paris");
        assertThat(approximate.getCountryCode()).isEqualTo("FR");
    }
}
