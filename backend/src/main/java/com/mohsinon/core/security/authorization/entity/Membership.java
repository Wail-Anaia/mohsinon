package com.mohsinon.core.security.authorization.entity;

import com.mohsinon.core.domain.BaseEntity;
import com.mohsinon.core.security.authorization.model.MembershipRole;
import com.mohsinon.core.security.authorization.model.MembershipStatus;
import com.mohsinon.core.security.authorization.model.ResourceContext;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

/**
 * Persistent entity representing a contextual membership of a user within a specific resource.
 * Example: User A is an IMAM of Mosque M1.
 */
@Entity
@Table(
        name = "memberships",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_membership_unique",
                        columnNames = {"user_id", "resource_type", "resource_id", "membership_role"}
                )
        },
        indexes = {
                @Index(name = "idx_memberships_user", columnList = "user_id"),
                @Index(name = "idx_memberships_resource", columnList = "resource_type, resource_id"),
                @Index(name = "idx_memberships_active", columnList = "user_id, status")
        }
)
public class Membership extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "resource_type", nullable = false, length = 50)
    private String resourceType;

    @Column(name = "resource_id", nullable = false)
    private UUID resourceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "membership_role", nullable = false, length = 50)
    private MembershipRole membershipRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private MembershipStatus status = MembershipStatus.ACTIVE;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "assigned_by")
    private UUID assignedBy;

    public Membership() {
    }

    public Membership(UUID userId, String resourceType, UUID resourceId, MembershipRole membershipRole, UUID assignedBy) {
        this(userId, resourceType, resourceId, membershipRole, null, assignedBy);
    }

    public Membership(UUID userId, String resourceType, UUID resourceId, MembershipRole membershipRole, Instant expiresAt, UUID assignedBy) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null.");
        }
        if (resourceType == null || resourceType.trim().isEmpty()) {
            throw new IllegalArgumentException("Resource type must not be null or empty.");
        }
        if (resourceId == null) {
            throw new IllegalArgumentException("Resource ID must not be null.");
        }
        if (membershipRole == null) {
            throw new IllegalArgumentException("Membership role must not be null.");
        }

        this.userId = userId;
        this.resourceType = resourceType.trim().toUpperCase(Locale.ROOT);
        this.resourceId = resourceId;
        this.membershipRole = membershipRole;
        this.expiresAt = expiresAt;
        this.assignedBy = assignedBy;
        this.status = MembershipStatus.ACTIVE;
    }

    public static Membership createActive(UUID userId, ResourceContext context, MembershipRole role, UUID assignedBy) {
        return new Membership(userId, context.resourceType(), context.resourceId(), role, assignedBy);
    }

    /**
     * Checks if this membership is currently active and within its valid time window.
     */
    public boolean isEffective() {
        if (status != MembershipStatus.ACTIVE) {
            return false;
        }
        if (expiresAt != null && expiresAt.isBefore(Instant.now())) {
            return false;
        }
        return true;
    }

    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(Instant.now());
    }

    public void revoke() {
        this.status = MembershipStatus.REVOKED;
    }

    public void expire() {
        this.status = MembershipStatus.EXPIRED;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType != null ? resourceType.trim().toUpperCase(Locale.ROOT) : null;
    }

    public UUID getResourceId() {
        return resourceId;
    }

    public void setResourceId(UUID resourceId) {
        this.resourceId = resourceId;
    }

    public MembershipRole getMembershipRole() {
        return membershipRole;
    }

    public void setMembershipRole(MembershipRole membershipRole) {
        this.membershipRole = membershipRole;
    }

    public MembershipStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipStatus status) {
        this.status = status;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public UUID getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(UUID assignedBy) {
        this.assignedBy = assignedBy;
    }
}
