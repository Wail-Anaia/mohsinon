package com.mohsinon.core.security.authorization.service;

import com.mohsinon.core.exception.ForbiddenException;
import com.mohsinon.core.security.authorization.entity.Membership;
import com.mohsinon.core.security.authorization.model.GlobalRoleType;
import com.mohsinon.core.security.authorization.model.MembershipRole;
import com.mohsinon.core.security.authorization.model.MembershipStatus;
import com.mohsinon.core.security.authorization.model.PermissionType;
import com.mohsinon.core.security.authorization.model.ResourceContext;
import com.mohsinon.core.security.authorization.repository.MembershipRepository;
import com.mohsinon.core.security.authorization.repository.UserGlobalRoleRepository;
import com.mohsinon.modules.identity.domain.User;
import com.mohsinon.modules.identity.domain.UserStatus;
import com.mohsinon.modules.identity.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultAuthorizationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserGlobalRoleRepository userGlobalRoleRepository;

    @Mock
    private MembershipRepository membershipRepository;

    @InjectMocks
    private DefaultAuthorizationService authorizationService;

    private User activeUser;
    private UUID userId;
    private UUID mosqueAId;
    private UUID mosqueBId;
    private UUID projectId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        mosqueAId = UUID.randomUUID();
        mosqueBId = UUID.randomUUID();
        projectId = UUID.randomUUID();

        activeUser = new User("imam_ahmed", "ahmed@example.com", "hash", "Ahmed", "Mansour", null);
        activeUser.setId(userId);
        activeUser.setStatus(UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("Unauthenticated / null user should be DENIED")
    void unauthenticatedOrNullUserShouldBeDenied() {
        assertThat(authorizationService.hasGlobalPermission(null, PermissionType.MOSQUE_VIEW)).isFalse();
        assertThat(authorizationService.hasPermission(null, PermissionType.MOSQUE_VIEW, ResourceContext.mosque(mosqueAId))).isFalse();
        assertThat(authorizationService.canManageMosque(null, mosqueAId)).isFalse();
        assertThat(authorizationService.isAdmin(null)).isFalse();
    }

    @Test
    @DisplayName("Unknown or null permission should be DENIED")
    void nullPermissionShouldBeDenied() {
        assertThat(authorizationService.hasGlobalPermission(userId, null)).isFalse();
        assertThat(authorizationService.hasPermission(userId, null, ResourceContext.mosque(mosqueAId))).isFalse();
    }

    @Test
    @DisplayName("User not found or suspended should be DENIED")
    void inactiveOrMissingUserShouldBeDenied() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThat(authorizationService.hasGlobalPermission(userId, PermissionType.MOSQUE_VIEW)).isFalse();

        activeUser.setStatus(UserStatus.SUSPENDED);
        when(userRepository.findById(userId)).thenReturn(Optional.of(activeUser));
        assertThat(authorizationService.hasGlobalPermission(userId, PermissionType.MOSQUE_VIEW)).isFalse();
        assertThat(authorizationService.isAdmin(userId)).isFalse();
    }

    @Test
    @DisplayName("ROLE_ADMIN should have global bypass (ALLOW for any permission)")
    void adminShouldHaveGlobalBypass() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(activeUser));
        when(userGlobalRoleRepository.findRoleNamesByUserId(userId)).thenReturn(Set.of(GlobalRoleType.ROLE_ADMIN.name()));
        when(userGlobalRoleRepository.userHasRoleName(userId, GlobalRoleType.ROLE_ADMIN.name())).thenReturn(true);

        assertThat(authorizationService.hasGlobalPermission(userId, PermissionType.ADMIN_ALL)).isTrue();
        assertThat(authorizationService.hasGlobalPermission(userId, PermissionType.MOSQUE_UPDATE)).isTrue();
        assertThat(authorizationService.canManageMosque(userId, mosqueAId)).isTrue();
        assertThat(authorizationService.isAdmin(userId)).isTrue();
    }

    @Test
    @DisplayName("Global role permissions should be granted without resource context (ALLOW)")
    void globalRolePermissionsGranted() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(activeUser));
        when(userGlobalRoleRepository.findRoleNamesByUserId(userId)).thenReturn(Set.of(GlobalRoleType.ROLE_DONOR.name()));

        assertThat(authorizationService.hasGlobalPermission(userId, PermissionType.DONATION_CREATE)).isTrue();
        assertThat(authorizationService.hasGlobalPermission(userId, PermissionType.DONATION_MANAGE)).isTrue();
        assertThat(authorizationService.hasGlobalPermission(userId, PermissionType.MOSQUE_UPDATE)).isFalse();
    }

    @Test
    @DisplayName("Active membership on target resource should be ALLOWED")
    void activeMembershipOnTargetResourceAllowed() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(activeUser));
        when(userGlobalRoleRepository.findRoleNamesByUserId(userId)).thenReturn(Set.of(GlobalRoleType.ROLE_USER.name()));

        Membership imamMembership = new Membership(userId, "MOSQUE", mosqueAId, MembershipRole.IMAM, null);
        when(membershipRepository.findEffectiveMemberships(eq(userId), eq("MOSQUE"), eq(mosqueAId), any(Instant.class)))
                .thenReturn(List.of(imamMembership));

        assertThat(authorizationService.hasPermission(userId, PermissionType.MOSQUE_UPDATE, ResourceContext.mosque(mosqueAId))).isTrue();
        assertThat(authorizationService.canManageMosque(userId, mosqueAId)).isTrue();
    }

    @Test
    @DisplayName("Cross-Mosque Isolation: Active membership on Mosque A must be DENIED on Mosque B")
    void crossMosqueIsolation() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(activeUser));
        when(userGlobalRoleRepository.findRoleNamesByUserId(userId)).thenReturn(Set.of(GlobalRoleType.ROLE_USER.name()));

        Membership imamMembershipA = new Membership(userId, "MOSQUE", mosqueAId, MembershipRole.IMAM, null);
        when(membershipRepository.findEffectiveMemberships(eq(userId), eq("MOSQUE"), eq(mosqueAId), any(Instant.class)))
                .thenReturn(List.of(imamMembershipA));
        when(membershipRepository.findEffectiveMemberships(eq(userId), eq("MOSQUE"), eq(mosqueBId), any(Instant.class)))
                .thenReturn(Collections.emptyList());

        assertThat(authorizationService.canManageMosque(userId, mosqueAId)).isTrue();
        assertThat(authorizationService.canManageMosque(userId, mosqueBId)).isFalse();
    }

    @Test
    @DisplayName("Membership for another resource type (e.g. PROJECT vs MOSQUE) should be DENIED")
    void differentResourceTypeDenied() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(activeUser));
        when(userGlobalRoleRepository.findRoleNamesByUserId(userId)).thenReturn(Set.of(GlobalRoleType.ROLE_USER.name()));

        when(membershipRepository.findEffectiveMemberships(eq(userId), eq("PROJECT"), eq(projectId), any(Instant.class)))
                .thenReturn(Collections.emptyList());

        assertThat(authorizationService.hasPermission(userId, PermissionType.PROJECT_MANAGE, ResourceContext.project(projectId))).isFalse();
    }

    @Test
    @DisplayName("REVOKED or EXPIRED membership should be DENIED")
    void inactiveMembershipStatusesDenied() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(activeUser));
        when(userGlobalRoleRepository.findRoleNamesByUserId(userId)).thenReturn(Set.of(GlobalRoleType.ROLE_USER.name()));

        // Revoked
        Membership revokedMembership = new Membership(userId, "MOSQUE", mosqueAId, MembershipRole.IMAM, null);
        revokedMembership.setStatus(MembershipStatus.REVOKED);
        when(membershipRepository.findEffectiveMemberships(eq(userId), eq("MOSQUE"), eq(mosqueAId), any(Instant.class)))
                .thenReturn(List.of(revokedMembership));

        assertThat(authorizationService.canManageMosque(userId, mosqueAId)).isFalse();

        // Expired status
        Membership expiredMembership = new Membership(userId, "MOSQUE", mosqueAId, MembershipRole.IMAM, null);
        expiredMembership.setStatus(MembershipStatus.EXPIRED);
        when(membershipRepository.findEffectiveMemberships(eq(userId), eq("MOSQUE"), eq(mosqueAId), any(Instant.class)))
                .thenReturn(List.of(expiredMembership));

        assertThat(authorizationService.canManageMosque(userId, mosqueAId)).isFalse();
    }

    @Test
    @DisplayName("Membership with past expiresAt date should be DENIED")
    void expiredDateDenied() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(activeUser));
        when(userGlobalRoleRepository.findRoleNamesByUserId(userId)).thenReturn(Set.of(GlobalRoleType.ROLE_USER.name()));

        Membership pastExpiry = new Membership(userId, "MOSQUE", mosqueAId, MembershipRole.IMAM, Instant.now().minusSeconds(120), null);
        when(membershipRepository.findEffectiveMemberships(eq(userId), eq("MOSQUE"), eq(mosqueAId), any(Instant.class)))
                .thenReturn(List.of(pastExpiry));

        assertThat(authorizationService.canManageMosque(userId, mosqueAId)).isFalse();
    }

    @Test
    @DisplayName("Valid membership but insufficient MembershipRole permission should be DENIED")
    void insufficientMembershipRolePermissionDenied() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(activeUser));
        when(userGlobalRoleRepository.findRoleNamesByUserId(userId)).thenReturn(Set.of(GlobalRoleType.ROLE_USER.name()));

        Membership treasurerMembership = new Membership(userId, "MOSQUE", mosqueAId, MembershipRole.TREASURER, null);
        when(membershipRepository.findEffectiveMemberships(eq(userId), eq("MOSQUE"), eq(mosqueAId), any(Instant.class)))
                .thenReturn(List.of(treasurerMembership));

        // Treasurer can manage donations on Mosque A
        assertThat(authorizationService.hasPermission(userId, PermissionType.DONATION_MANAGE, ResourceContext.mosque(mosqueAId))).isTrue();
        // But treasurer CANNOT update mosque or manage members
        assertThat(authorizationService.hasPermission(userId, PermissionType.MOSQUE_UPDATE, ResourceContext.mosque(mosqueAId))).isFalse();
        assertThat(authorizationService.hasPermission(userId, PermissionType.MOSQUE_MANAGE_MEMBERS, ResourceContext.mosque(mosqueAId))).isFalse();
    }

    @Test
    @DisplayName("requireGlobalPermission and requirePermission should throw ForbiddenException when access is denied")
    void requireMethodsShouldThrowForbidden() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(activeUser));
        when(userGlobalRoleRepository.findRoleNamesByUserId(userId)).thenReturn(Set.of(GlobalRoleType.ROLE_USER.name()));
        when(membershipRepository.findEffectiveMemberships(eq(userId), eq("MOSQUE"), eq(mosqueBId), any(Instant.class)))
                .thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> authorizationService.requireGlobalPermission(userId, PermissionType.MOSQUE_UPDATE))
                .isInstanceOf(ForbiddenException.class)
                .matches(ex -> "INSUFFICIENT_PERMISSIONS".equals(((ForbiddenException) ex).getErrorCode()))
                .hasMessageContaining("You do not have the required global permission");

        assertThatThrownBy(() -> authorizationService.requirePermission(userId, PermissionType.MOSQUE_UPDATE, ResourceContext.mosque(mosqueBId)))
                .isInstanceOf(ForbiddenException.class)
                .matches(ex -> "INSUFFICIENT_CONTEXTUAL_PERMISSIONS".equals(((ForbiddenException) ex).getErrorCode()))
                .hasMessageContaining("You do not have the required permission");

        assertThatThrownBy(() -> authorizationService.requirePermission(userId, PermissionType.MOSQUE_UPDATE, "MOSQUE", mosqueBId))
                .isInstanceOf(ForbiddenException.class);
    }
}
