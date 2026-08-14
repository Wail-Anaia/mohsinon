package com.mohsinon.core.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Offset.offset;

class GeoLocationTest {

    @Test
    @DisplayName("Should accurately calculate distance between Mecca and Medina (~340 km)")
    void shouldCalculateDistanceBetweenMeccaAndMedina() {
        // Mecca: 21.4225° N, 39.8262° E
        GeoLocation mecca = GeoLocation.of(21.4225, 39.8262, "Mecca", "SA");
        // Medina: 24.4672° N, 39.6111° E
        GeoLocation medina = GeoLocation.of(24.4672, 39.6111, "Medina", "SA");

        double distanceKm = mecca.distanceToInKm(medina);

        // Great circle distance is ~340 km
        assertThat(distanceKm).isCloseTo(340.0, offset(15.0));
    }

    @Test
    @DisplayName("Should accurately calculate distance between Paris and Lyon (~392 km)")
    void shouldCalculateDistanceBetweenParisAndLyon() {
        GeoLocation paris = GeoLocation.of(48.8566, 2.3522, "Paris", "FR");
        GeoLocation lyon = GeoLocation.of(45.7640, 4.8357, "Lyon", "FR");

        double distanceKm = paris.distanceToInKm(lyon);

        assertThat(distanceKm).isCloseTo(392.0, offset(10.0));
    }

    @Test
    @DisplayName("Should detect when point is within specified radius")
    void shouldDetectRadiusProximity() {
        GeoLocation center = GeoLocation.of(48.8566, 2.3522, "Paris", "FR");
        GeoLocation nearby = GeoLocation.of(48.8600, 2.3500, "Paris", "FR"); // ~400m away
        GeoLocation farAway = GeoLocation.of(45.7640, 4.8357, "Lyon", "FR"); // ~392km away

        assertThat(nearby.isWithinRadiusOf(center, 5.0)).isTrue();
        assertThat(farAway.isWithinRadiusOf(center, 10.0)).isFalse();
    }

    @Test
    @DisplayName("Should generate privacy-preserving blurred coordinates")
    void shouldBlurCoordinatesForPrivacy() {
        GeoLocation exact = GeoLocation.of(48.856613, 2.352222, "Paris", "FR");
        GeoLocation approximate = exact.toApproximate();

        assertThat(approximate.getLatitude()).isEqualTo(48.86);
        assertThat(approximate.getLongitude()).isEqualTo(2.35);
        assertThat(approximate.getCity()).isEqualTo("Paris");
        assertThat(approximate.getCountryCode()).isEqualTo("FR");
    }

    @Test
    @DisplayName("Should reject invalid latitude out of bounds")
    void shouldRejectInvalidLatitude() {
        assertThatThrownBy(() -> GeoLocation.of(95.0, 2.35))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Latitude must be between -90.0 and 90.0");
    }

    @Test
    @DisplayName("Should reject invalid longitude out of bounds")
    void shouldRejectInvalidLongitude() {
        assertThatThrownBy(() -> GeoLocation.of(48.85, 185.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Longitude must be between -180.0 and 180.0");
    }
}
