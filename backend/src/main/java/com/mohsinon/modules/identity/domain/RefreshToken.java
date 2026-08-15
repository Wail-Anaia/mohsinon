package com.mohsinon.modules.identity.domain;

import com.mohsinon.core.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Entity representing an issued Refresh Token with rotation tracking and hashed storage.
 */
@Entity
@Table(name = "refresh_tokens", indexes = {
        @Index(name = "idx_refresh_tokens_hash", columnList = "token_hash"),
        @Index(name = "idx_refresh_tokens_user", columnList = "user_id"),
        @Index(name = "idx_refresh_tokens_expires", columnList = "expires_at")
})
public class RefreshToken extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "token_hash", nullable = false, length = 64)
    private String tokenHash;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @Column(name = "replaced_by_token_hash", length = 64)
    private String replacedByTokenHash;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 255)
    private String userAgent;

    public RefreshToken() {
    }

    public RefreshToken(UUID userId, String tokenHash, Instant expiresAt, String ipAddress, String userAgent) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required for refresh token.");
        }
        if (tokenHash == null || tokenHash.trim().isEmpty()) {
            throw new IllegalArgumentException("Token hash is required.");
        }
        if (expiresAt == null) {
            throw new IllegalArgumentException("Expiration timestamp is required.");
        }

        this.userId = userId;
        this.tokenHash = tokenHash.trim();
        this.expiresAt = expiresAt;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent != null && userAgent.length() > 255 ? userAgent.substring(0, 255) : userAgent;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiresAt);
    }

    public boolean isRevoked() {
        return this.revokedAt != null;
    }

    public boolean isActive() {
        return !isExpired() && !isRevoked();
    }

    public void revoke() {
        if (this.revokedAt == null) {
            this.revokedAt = Instant.now();
        }
    }

    public void replaceWith(String newTokenHash) {
        revoke();
        this.replacedByTokenHash = newTokenHash;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Instant getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(Instant revokedAt) {
        this.revokedAt = revokedAt;
    }

    public String getReplacedByTokenHash() {
        return replacedByTokenHash;
    }

    public void setReplacedByTokenHash(String replacedByTokenHash) {
        this.replacedByTokenHash = replacedByTokenHash;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent != null && userAgent.length() > 255 ? userAgent.substring(0, 255) : userAgent;
    }
}
