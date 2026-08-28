package com.mohsinon.core.security.authorization.entity;

import com.mohsinon.core.security.authorization.model.MembershipRole;
import com.mohsinon.core.security.authorization.model.MembershipStatus;
import com.mohsinon.core.security.authorization.model.ResourceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MembershipTest {

    @Test
    @DisplayName("ACTIVE membership without expiration date should be effective")
    void activeWithoutExpiryShouldBeEffective() {
        UUID userId = UUID.randomUUID();
        UUID mosqueId = UUID.randomUUID();
        UUID assignedBy = UUID.randomUUID();

        Membership membership = new Membership(userId, "MOSQUE", mosqueId, MembershipRole.IMAM, assignedBy);

        assertThat(membership.isEffective()).isTrue();
        assertThat(membership.getStatus()).isEqualTo(MembershipStatus.ACTIVE);
        assertThat(membership.getResourceType()).isEqualTo("MOSQUE");
        assertThat(membership.getResourceId()).isEqualTo(mosqueId);
        assertThat(membership.getUserId()).isEqualTo(userId);
        assertThat(membership.getMembershipRole()).isEqualTo(MembershipRole.IMAM);
        assertThat(membership.getAssignedBy()).isEqualTo(assignedBy);
        assertThat(membership.isExpired()).isFalse();
    }

    @Test
    @DisplayName("ACTIVE membership created via ResourceContext factory method should be effective")
    void activeViaFactoryMethod() {
        UUID userId = UUID.randomUUID();
        UUID mosqueId = UUID.randomUUID();
        UUID assignedBy = UUID.randomUUID();
        ResourceContext context = ResourceContext.mosque(mosqueId);

        Membership membership = Membership.createActive(userId, context, MembershipRole.IMAM, assignedBy);

        assertThat(membership.isEffective()).isTrue();
        assertThat(membership.getResourceType()).isEqualTo("MOSQUE");
        assertThat(membership.getResourceId()).isEqualTo(mosqueId);
        assertThat(membership.getAssignedBy()).isEqualTo(assignedBy);
    }

    @Test
    @DisplayName("ACTIVE membership with future expiration date should be effective")
    void activeWithFutureExpiryShouldBeEffective() {
        UUID userId = UUID.randomUUID();
        UUID mosqueId = UUID.randomUUID();
        Instant future = Instant.now().plusSeconds(3600);

        Membership membership = new Membership(userId, "MOSQUE", mosqueId, MembershipRole.IMAM, future, null);

        assertThat(membership.isEffective()).isTrue();
        assertThat(membership.isExpired()).isFalse();
        assertThat(membership.getExpiresAt()).isEqualTo(future);
    }

    @Test
    @DisplayName("Membership with past expiration date should NOT be effective and should be expired")
    void pastExpiryShouldNotBeEffective() {
        UUID userId = UUID.randomUUID();
        UUID mosqueId = UUID.randomUUID();
        Instant past = Instant.now().minusSeconds(3600);

        Membership membership = new Membership(userId, "MOSQUE", mosqueId, MembershipRole.IMAM, past, null);

        assertThat(membership.isEffective()).isFalse();
        assertThat(membership.isExpired()).isTrue();
    }

    @Test
    @DisplayName("REVOKED membership should NOT be effective")
    void revokedMembershipShouldNotBeEffective() {
        UUID userId = UUID.randomUUID();
        UUID mosqueId = UUID.randomUUID();

        Membership membership = new Membership(userId, "MOSQUE", mosqueId, MembershipRole.IMAM, null);
        membership.revoke();

        assertThat(membership.isEffective()).isFalse();
        assertThat(membership.getStatus()).isEqualTo(MembershipStatus.REVOKED);
    }

    @Test
    @DisplayName("PENDING_APPROVAL membership should NOT be effective")
    void pendingApprovalShouldNotBeEffective() {
        UUID userId = UUID.randomUUID();
        UUID mosqueId = UUID.randomUUID();

        Membership membership = new Membership(userId, "MOSQUE", mosqueId, MembershipRole.IMAM, null);
        membership.setStatus(MembershipStatus.PENDING_APPROVAL);

        assertThat(membership.isEffective()).isFalse();
        assertThat(membership.getStatus()).isEqualTo(MembershipStatus.PENDING_APPROVAL);
    }

    @Test
    @DisplayName("EXPIRED status membership should NOT be effective")
    void expiredStatusShouldNotBeEffective() {
        UUID userId = UUID.randomUUID();
        UUID mosqueId = UUID.randomUUID();

        Membership membership = new Membership(userId, "MOSQUE", mosqueId, MembershipRole.IMAM, null);
        membership.expire();

        assertThat(membership.isEffective()).isFalse();
        assertThat(membership.getStatus()).isEqualTo(MembershipStatus.EXPIRED);
    }

    @Test
    @DisplayName("Should reject null or invalid required fields during construction")
    void shouldRejectInvalidFields() {
        UUID userId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();

        // Null userId
        assertThatThrownBy(() -> new Membership(null, "MOSQUE", resourceId, MembershipRole.IMAM, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User ID must not be null");

        // Null/empty resourceType
        assertThatThrownBy(() -> new Membership(userId, null, resourceId, MembershipRole.IMAM, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Resource type must not be null or empty");

        assertThatThrownBy(() -> new Membership(userId, "   ", resourceId, MembershipRole.IMAM, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Resource type must not be null or empty");

        // Null resourceId
        assertThatThrownBy(() -> new Membership(userId, "MOSQUE", null, MembershipRole.IMAM, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Resource ID must not be null");

        // Null membershipRole
        assertThatThrownBy(() -> new Membership(userId, "MOSQUE", resourceId, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Membership role must not be null");
    }
}
