package com.mohsinon.core.reputation;

import com.mohsinon.core.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Immutable ledger entry recording every verifiable impact point allocation or deduction.
 * Ensures strict auditability and prevents arbitrary direct balance mutation.
 */
@Entity
@Table(name = "impact_transactions", indexes = {
        @Index(name = "idx_impact_tx_user_id", columnList = "user_id"),
        @Index(name = "idx_impact_tx_created_at", columnList = "created_at"),
        @Index(name = "idx_impact_tx_reference", columnList = "reference_type, reference_id")
})
public class ImpactTransaction extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private ImpactTransactionType type;

    @Column(name = "points", nullable = false)
    private int points;

    @Column(name = "reference_type", nullable = false, length = 50)
    private String referenceType;

    @Column(name = "reference_id")
    private UUID referenceId;

    @Column(name = "reason", nullable = false, length = 255)
    private String reason;

    public ImpactTransaction() {
    }

    public ImpactTransaction(UUID userId, ImpactTransactionType type, int points,
                             String referenceType, UUID referenceId, String reason) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required for impact transaction.");
        }
        if (type == null) {
            throw new IllegalArgumentException("Transaction type is required.");
        }
        if (referenceType == null || referenceType.trim().isEmpty()) {
            throw new IllegalArgumentException("Reference type is required.");
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction reason is required.");
        }

        this.userId = userId;
        this.type = type;
        this.points = points;
        this.referenceType = referenceType.trim().toUpperCase();
        this.referenceId = referenceId;
        this.reason = reason.trim();
    }

    public static ImpactTransaction earned(UUID userId, int points, String referenceType, UUID referenceId, String reason) {
        if (points <= 0) {
            throw new IllegalArgumentException("Earned points must be greater than zero.");
        }
        return new ImpactTransaction(userId, ImpactTransactionType.EARNED, points, referenceType, referenceId, reason);
    }

    public static ImpactTransaction spent(UUID userId, int points, String referenceType, UUID referenceId, String reason) {
        if (points <= 0) {
            throw new IllegalArgumentException("Spent points must be greater than zero.");
        }
        return new ImpactTransaction(userId, ImpactTransactionType.SPENT, -points, referenceType, referenceId, reason);
    }

    public static ImpactTransaction adjusted(UUID userId, int netPoints, String referenceType, UUID referenceId, String reason) {
        return new ImpactTransaction(userId, ImpactTransactionType.ADJUSTED, netPoints, referenceType, referenceId, reason);
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public ImpactTransactionType getType() {
        return type;
    }

    public void setType(ImpactTransactionType type) {
        this.type = type;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public UUID getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(UUID referenceId) {
        this.referenceId = referenceId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
