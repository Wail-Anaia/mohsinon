package com.mohsinon.core.security.authorization.service;

import com.mohsinon.core.exception.ResourceNotFoundException;
import com.mohsinon.core.security.authorization.entity.Membership;
import com.mohsinon.core.security.authorization.model.MembershipRole;
import com.mohsinon.core.security.authorization.model.MembershipStatus;
import com.mohsinon.core.security.authorization.model.ResourceContext;
import com.mohsinon.core.security.authorization.repository.MembershipRepository;
import com.mohsinon.modules.identity.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MembershipServiceTest {

    @Mock
    private MembershipRepository membershipRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MembershipService membershipService;

    private UUID userId;
    private UUID mosqueId;
    private UUID assignedBy;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        mosqueId = UUID.randomUUID();
        assignedBy = UUID.randomUUID();
    }

    @Test
    @DisplayName("Should successfully assign new membership when user exists")
    void shouldAssignNewMembership() {
        when(userRepository.existsById(userId)).thenReturn(true);
        when(membershipRepository.findByUserIdAndResourceTypeAndResourceIdAndMembershipRole(
                eq(userId), eq("MOSQUE"), eq(mosqueId), eq(MembershipRole.IMAM)))
                .thenReturn(Optional.empty());
        when(membershipRepository.save(any(Membership.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Membership result = membershipService.assignMosqueMembership(userId, mosqueId, MembershipRole.IMAM, assignedBy);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getResourceType()).isEqualTo("MOSQUE");
        assertThat(result.getResourceId()).isEqualTo(mosqueId);
        assertThat(result.getMembershipRole()).isEqualTo(MembershipRole.IMAM);
        assertThat(result.getStatus()).isEqualTo(MembershipStatus.ACTIVE);
        assertThat(result.getAssignedBy()).isEqualTo(assignedBy);
    }

    @Test
    @DisplayName("Should reactivate existing membership and update expiresAt/assignedBy")
    void shouldReactivateExistingMembership() {
        when(userRepository.existsById(userId)).thenReturn(true);

        Membership existing = new Membership(userId, "MOSQUE", mosqueId, MembershipRole.IMAM, null);
        existing.setStatus(MembershipStatus.REVOKED);

        when(membershipRepository.findByUserIdAndResourceTypeAndResourceIdAndMembershipRole(
                eq(userId), eq("MOSQUE"), eq(mosqueId), eq(MembershipRole.IMAM)))
                .thenReturn(Optional.of(existing));
        when(membershipRepository.save(any(Membership.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Instant newExpiry = Instant.now().plusSeconds(7200);
        Membership result = membershipService.assignMembership(userId, ResourceContext.mosque(mosqueId), MembershipRole.IMAM, newExpiry, assignedBy);

        assertThat(result.getStatus()).isEqualTo(MembershipStatus.ACTIVE);
        assertThat(result.getExpiresAt()).isEqualTo(newExpiry);
        assertThat(result.getAssignedBy()).isEqualTo(assignedBy);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when assigning membership to non-existent user")
    void shouldThrowWhenUserNotFound() {
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThatThrownBy(() -> membershipService.assignMosqueMembership(userId, mosqueId, MembershipRole.IMAM, assignedBy))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User");
    }

    @Test
    @DisplayName("Should reject invalid or null required parameters")
    void shouldRejectInvalidParams() {
        assertThatThrownBy(() -> membershipService.assignMembership(null, "MOSQUE", mosqueId, MembershipRole.IMAM, null, assignedBy))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> membershipService.assignMembership(userId, "", mosqueId, MembershipRole.IMAM, null, assignedBy))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> membershipService.assignMembership(userId, "MOSQUE", null, MembershipRole.IMAM, null, assignedBy))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> membershipService.assignMembership(userId, "MOSQUE", mosqueId, null, null, assignedBy))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> membershipService.assignMembership(userId, (ResourceContext) null, MembershipRole.IMAM, assignedBy))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should revoke membership by ID")
    void shouldRevokeById() {
        UUID membershipId = UUID.randomUUID();
        Membership membership = new Membership(userId, "MOSQUE", mosqueId, MembershipRole.IMAM, assignedBy);

        when(membershipRepository.findById(membershipId)).thenReturn(Optional.of(membership));
        when(membershipRepository.save(any(Membership.class))).thenAnswer(inv -> inv.getArgument(0));

        membershipService.revokeMembership(membershipId);

        assertThat(membership.getStatus()).isEqualTo(MembershipStatus.REVOKED);
        verify(membershipRepository).save(membership);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when revoking non-existent membership ID")
    void shouldThrowWhenRevokingNonExistentId() {
        UUID membershipId = UUID.randomUUID();
        when(membershipRepository.findById(membershipId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> membershipService.revokeMembership(membershipId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Membership");
    }

    @Test
    @DisplayName("Should revoke specific membership by user and resource")
    void shouldRevokeByTuple() {
        Membership membership = new Membership(userId, "MOSQUE", mosqueId, MembershipRole.IMAM, assignedBy);

        when(membershipRepository.findByUserIdAndResourceTypeAndResourceIdAndMembershipRole(
                eq(userId), eq("MOSQUE"), eq(mosqueId), eq(MembershipRole.IMAM)))
                .thenReturn(Optional.of(membership));
        when(membershipRepository.save(any(Membership.class))).thenAnswer(inv -> inv.getArgument(0));

        membershipService.revokeMembership(userId, "MOSQUE", mosqueId, MembershipRole.IMAM);

        assertThat(membership.getStatus()).isEqualTo(MembershipStatus.REVOKED);
        verify(membershipRepository).save(membership);
    }

    @Test
    @DisplayName("Should retrieve effective memberships for user in resource context")
    void shouldRetrieveEffectiveMemberships() {
        Membership activeMembership = new Membership(userId, "MOSQUE", mosqueId, MembershipRole.IMAM, assignedBy);

        when(membershipRepository.findEffectiveMemberships(eq(userId), eq("MOSQUE"), eq(mosqueId), any(Instant.class)))
                .thenReturn(List.of(activeMembership));

        List<Membership> effective = membershipService.getEffectiveMemberships(userId, ResourceContext.mosque(mosqueId));

        assertThat(effective).hasSize(1);
        assertThat(effective.get(0).getMembershipRole()).isEqualTo(MembershipRole.IMAM);
    }

    @Test
    @DisplayName("Should retrieve user active memberships and resource active memberships")
    void shouldRetrieveActiveMemberships() {
        Membership active = new Membership(userId, "MOSQUE", mosqueId, MembershipRole.IMAM, assignedBy);

        when(membershipRepository.findByUserIdAndStatus(userId, MembershipStatus.ACTIVE))
                .thenReturn(List.of(active));
        when(membershipRepository.findByResourceTypeAndResourceIdAndStatus("MOSQUE", mosqueId, MembershipStatus.ACTIVE))
                .thenReturn(List.of(active));

        List<Membership> userMemberships = membershipService.getUserActiveMemberships(userId);
        List<Membership> resourceMemberships = membershipService.getResourceActiveMemberships("MOSQUE", mosqueId);

        assertThat(userMemberships).hasSize(1);
        assertThat(resourceMemberships).hasSize(1);
    }
}
