package com.mohsinon.core.security.authorization.entity;

import com.mohsinon.core.security.authorization.model.MembershipRole;
import com.mohsinon.core.security.authorization.model.MembershipStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MembershipTest {

    @Test
    @DisplayName("Active membership without expiration date should be effective")
    void activeWithoutExpiryShouldBeEffective() {
        UUID userId = UUID.randomUUID();
        UUID mosqueId = UUID.randomUUID();

        Membership membership = new Membership(userId, "MOSQUE", mosqueId, MembershipRole.IMAM, null);

        assertThat(membership.isEffective()).isTrue();
        assertThat(membership.getStatus()).isEqualTo(MembershipStatus.ACTIVE);
        assertThat(membership.getResourceType()).isEqualTo("MOSQUE");
    }

    @Test
    @DisplayName("Active membership with future expiration date should be effective")
    void activeWithFutureExpiryShouldBeEffective() {
        UUID userId = UUID.randomUUID();
        UUID mosqueId = UUID.randomUUID();
        Instant future = Instant.now().plusSeconds(3600);

        Membership membership = new Membership(userId, "MOSQUE", mosqueId, MembershipRole.IMAM, future, null);

        assertThat(membership.isEffective()).isTrue();
        assertThat(membership.isExpired()).isFalse();
    }

    @Test
    @DisplayName("Membership with past expiration date should NOT be effective")
    void pastExpiryShouldNotBeEffective() {
        UUID userId = UUID.randomUUID();
        UUID mosqueId = UUID.randomUUID();
        Instant past = Instant.now().minusSeconds(3600);

        Membership membership = new Membership(userId, "MOSQUE", mosqueId, MembershipRole.IMAM, past, null);

        assertThat(membership.isEffective()).isFalse();
        assertThat(membership.isExpired()).isTrue();
    }

    @Test
    @DisplayName("Revoked membership should NOT be effective")
    void revokedMembershipShouldNotBeEffective() {
        UUID userId = UUID.randomUUID();
        UUID mosqueId = UUID.randomUUID();

        Membership membership = new Membership(userId, "MOSQUE", mosqueId, MembershipRole.IMAM, null);
        membership.revoke();

        assertThat(membership.isEffective()).isFalse();
        assertThat(membership.getStatus()).isEqualTo(MembershipStatus.REVOKED);
    }
}
