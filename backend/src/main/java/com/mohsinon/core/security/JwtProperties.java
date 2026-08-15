package com.mohsinon.core.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mohsinon.security.jwt")
public class JwtProperties {

    /**
     * Secret key for signing JWTs (must be at least 256 bits / 32 bytes).
     */
    private String secret = "mohsinon-super-secret-jwt-signing-key-for-development-must-be-changed-in-production-2026";

    /**
     * Access token validity in seconds (default: 15 minutes = 900s).
     */
    private long accessTokenExpirationSeconds = 900;

    /**
     * Refresh token validity in seconds (default: 7 days = 604800s).
     */
    private long refreshTokenExpirationSeconds = 604800;

    /**
     * Token issuer.
     */
    private String issuer = "mohsinon.org";

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getAccessTokenExpirationSeconds() {
        return accessTokenExpirationSeconds;
    }

    public void setAccessTokenExpirationSeconds(long accessTokenExpirationSeconds) {
        this.accessTokenExpirationSeconds = accessTokenExpirationSeconds;
    }

    public long getRefreshTokenExpirationSeconds() {
        return refreshTokenExpirationSeconds;
    }

    public void setRefreshTokenExpirationSeconds(long refreshTokenExpirationSeconds) {
        this.refreshTokenExpirationSeconds = refreshTokenExpirationSeconds;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}
