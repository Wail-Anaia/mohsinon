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

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        mosqueAId = UUID.randomUUID();
        mosqueBId = UUID.randomUUID();

        activeUser = new User("imam_ahmed", "ahmed@example.com", "hash", "Ahmed", "Mansour", null);
        activeUser.setId(userId);
        activeUser.setStatus(UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("Should deny access when user is not found or inactive")
    void shouldDenyWhenUserInactive() {
        activeUser.setStatus(UserStatus.SUSPENDED);
        when(userRepository.findById(userId)).thenReturn(Optional.of(activeUser));

        boolean allowed = authorizationService.hasGlobalPermission(userId, PermissionType.MOSQUE_VIEW);

        assertThat(allowed).isFalse();
    }

    @Test
    @DisplayName("ROLE_ADMIN should bypass all checks and be allowed for any permission")
    void adminShouldBeAllowedForAnyAction() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(activeUser));
        when(userGlobalRoleRepository.findRoleNamesByUserId(userId)).thenReturn(Set.of(GlobalRoleType.ROLE_ADMIN.name()));

        boolean globalAllowed = authorizationService.hasGlobalPermission(userId, PermissionType.ADMIN_ALL);
        boolean contextualAllowed = authorizationService.canManageMosque(userId, mosqueAId);

        assertThat(globalAllowed).isTrue();
        assertThat(contextualAllowed).isTrue();
    }

    @Test
    @DisplayName("Global role permissions should be granted without requiring a resource context")
    void globalRolePermissionsGranted() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(activeUser));
        when(userGlobalRoleRepository.findRoleNamesByUserId(userId)).thenReturn(Set.of(GlobalRoleType.ROLE_DONOR.name()));

        boolean canCreateDonation = authorizationService.hasGlobalPermission(userId, PermissionType.DONATION_CREATE);
        boolean canUpdateMosque = authorizationService.hasGlobalPermission(userId, PermissionType.MOSQUE_UPDATE);

        assertThat(canCreateDonation).isTrue();
        assertThat(canUpdateMosque).isFalse();
    }

    @Test
    @DisplayName("Contextual permission should be allowed for target mosque but DENIED for other mosques (Cross-Mosque Isolation)")
    void crossMosqueIsolation() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(activeUser));
        when(userGlobalRoleRepository.findRoleNamesByUserId(userId)).thenReturn(Set.of(GlobalRoleType.ROLE_USER.name()));

        Membership imamMembershipA = new Membership(userId, "MOSQUE", mosqueAId, MembershipRole.IMAM, null);

        when(membershipRepository.findEffectiveMemberships(eq(userId), eq("MOSQUE"), eq(mosqueAId), any(Instant.class)))
                .thenReturn(List.of(imamMembershipA));
        when(membershipRepository.findEffectiveMemberships(eq(userId), eq("MOSQUE"), eq(mosqueBId), any(Instant.class)))
                .thenReturn(Collections.emptyList());

        boolean canManageA = authorizationService.canManageMosque(userId, mosqueAId);
        boolean canManageB = authorizationService.canManageMosque(userId, mosqueBId);

        assertThat(canManageA).isTrue();
        assertThat(canManageB).isFalse();
    }

    @Test
    @DisplayName("Revoked membership should be denied access")
    void revokedMembershipDenied() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(activeUser));
        when(userGlobalRoleRepository.findRoleNamesByUserId(userId)).thenReturn(Set.of(GlobalRoleType.ROLE_USER.name()));

        Membership revokedMembership = new Membership(userId, "MOSQUE", mosqueAId, MembershipRole.IMAM, null);
        revokedMembership.setStatus(MembershipStatus.REVOKED);

        when(membershipRepository.findEffectiveMemberships(eq(userId), eq("MOSQUE"), eq(mosqueAId), any(Instant.class)))
                .thenReturn(List.of(revokedMembership));

        boolean canManage = authorizationService.canManageMosque(userId, mosqueAId);

        assertThat(canManage).isFalse();
    }

    @Test
    @DisplayName("requireGlobalPermission and requirePermission should throw ForbiddenException when denied")
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
    }
}
