package com.mohsinon.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Value Object representing geographic coordinates with spatial calculations and privacy safeguards.
 * Embeddable within JPA entities (Mosques, Initiatives, Projects, approximate user locations).
 */
@Embeddable
public class GeoLocation implements Serializable {

    private static final double EARTH_RADIUS_KM = 6371.0;
    private static final double MIN_LATITUDE = -90.0;
    private static final double MAX_LATITUDE = 90.0;
    private static final double MIN_LONGITUDE = -180.0;
    private static final double MAX_LONGITUDE = 180.0;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "country_code", length = 3)
    private String countryCode;

    public GeoLocation() {
    }

    public GeoLocation(Double latitude, Double longitude, String city, String countryCode) {
        validateCoordinates(latitude, longitude);
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city != null ? city.trim() : null;
        this.countryCode = countryCode != null ? countryCode.trim().toUpperCase() : null;
    }

    public static GeoLocation of(Double latitude, Double longitude) {
        return new GeoLocation(latitude, longitude, null, null);
    }

    public static GeoLocation of(Double latitude, Double longitude, String city, String countryCode) {
        return new GeoLocation(latitude, longitude, city, countryCode);
    }

    public static GeoLocation ofCityOnly(String city, String countryCode) {
        return new GeoLocation(null, null, city, countryCode);
    }

    private void validateCoordinates(Double lat, Double lon) {
        if (lat != null && (lat < MIN_LATITUDE || lat > MAX_LATITUDE)) {
            throw new IllegalArgumentException("Latitude must be between " + MIN_LATITUDE + " and " + MAX_LATITUDE + " degrees.");
        }
        if (lon != null && (lon < MIN_LONGITUDE || lon > MAX_LONGITUDE)) {
            throw new IllegalArgumentException("Longitude must be between " + MIN_LONGITUDE + " and " + MAX_LONGITUDE + " degrees.");
        }
    }

    /**
     * Calculates the great-circle distance between two geographic points using the Haversine formula.
     *
     * @param other Target location
     * @return Distance in kilometers, or Double.MAX_VALUE if coordinates are missing.
     */
    public double distanceToInKm(GeoLocation other) {
        if (other == null || this.latitude == null || this.longitude == null ||
                other.latitude == null || other.longitude == null) {
            return Double.MAX_VALUE;
        }

        double lat1Rad = Math.toRadians(this.latitude);
        double lon1Rad = Math.toRadians(this.longitude);
        double lat2Rad = Math.toRadians(other.latitude);
        double lon2Rad = Math.toRadians(other.longitude);

        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    /**
     * Checks whether this location is within the specified radius of another location.
     */
    public boolean isWithinRadiusOf(GeoLocation center, double radiusKm) {
        if (center == null || radiusKm < 0) return false;
        return distanceToInKm(center) <= radiusKm;
    }

    /**
     * Returns a privacy-preserving blurred location (rounds coordinates to ~1-2km precision).
     * Used for public display of personal locations without exposing exact residences.
     */
    public GeoLocation toApproximate() {
        if (this.latitude == null || this.longitude == null) {
            return new GeoLocation(null, null, this.city, this.countryCode);
        }
        // Rounding to 2 decimal places provides ~1.1km accuracy at equator
        double roundedLat = Math.round(this.latitude * 100.0) / 100.0;
        double roundedLon = Math.round(this.longitude * 100.0) / 100.0;
        return new GeoLocation(roundedLat, roundedLon, this.city, this.countryCode);
    }

    public boolean hasCoordinates() {
        return this.latitude != null && this.longitude != null;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        validateCoordinates(latitude, this.longitude);
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        validateCoordinates(this.latitude, longitude);
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city != null ? city.trim() : null;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode != null ? countryCode.trim().toUpperCase() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeoLocation that = (GeoLocation) o;
        return Objects.equals(latitude, that.latitude) &&
                Objects.equals(longitude, that.longitude) &&
                Objects.equals(city, that.city) &&
                Objects.equals(countryCode, that.countryCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude, city, countryCode);
    }

    @Override
    public String toString() {
        return "GeoLocation{" +
                "lat=" + latitude +
                ", lon=" + longitude +
                ", city='" + city + '\'' +
                ", countryCode='" + countryCode + '\'' +
                '}';
    }
}
