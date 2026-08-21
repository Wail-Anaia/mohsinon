package com.mohsinon.core.security.authorization.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;
import java.util.UUID;

/**
 * Persistent join entity linking a User to a GlobalRole.
 */
@Entity
@Table(
        name = "user_global_roles",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_global_role", columnNames = {"user_id", "role_id"})
        },
        indexes = {
                @Index(name = "idx_user_global_roles_user", columnList = "user_id"),
                @Index(name = "idx_user_global_roles_role", columnList = "role_id")
        }
)
public class UserGlobalRole {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "role_id", nullable = false)
    private UUID roleId;

    @Column(name = "assigned_at", nullable = false)
    private Instant assignedAt = Instant.now();

    @Column(name = "assigned_by")
    private UUID assignedBy;

    public UserGlobalRole() {
    }

    public UserGlobalRole(UUID userId, UUID roleId, UUID assignedBy) {
        this.userId = userId;
        this.roleId = roleId;
        this.assignedAt = Instant.now();
        this.assignedBy = assignedBy;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public Instant getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(Instant assignedAt) {
        this.assignedAt = assignedAt;
    }

    public UUID getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(UUID assignedBy) {
        this.assignedBy = assignedBy;
    }
}
