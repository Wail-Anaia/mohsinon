package com.mohsinon.core.security.authorization.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PermissionRegistryTest {

    @Test
    @DisplayName("ROLE_ADMIN should grant all permissions including ADMIN_ALL")
    void adminShouldGrantAllPermissions() {
        for (PermissionType permission : PermissionType.values()) {
            assertThat(PermissionRegistry.doesGlobalRoleGrant(GlobalRoleType.ROLE_ADMIN, permission)).isTrue();
        }
    }

    @Test
    @DisplayName("ROLE_USER should grant basic global permissions but not administrative ones")
    void userShouldHaveBasicPermissions() {
        assertThat(PermissionRegistry.doesGlobalRoleGrant(GlobalRoleType.ROLE_USER, PermissionType.DONATION_CREATE)).isTrue();
        assertThat(PermissionRegistry.doesGlobalRoleGrant(GlobalRoleType.ROLE_USER, PermissionType.INITIATIVE_CREATE)).isTrue();
        assertThat(PermissionRegistry.doesGlobalRoleGrant(GlobalRoleType.ROLE_USER, PermissionType.MOSQUE_VIEW)).isTrue();

        assertThat(PermissionRegistry.doesGlobalRoleGrant(GlobalRoleType.ROLE_USER, PermissionType.MOSQUE_UPDATE)).isFalse();
        assertThat(PermissionRegistry.doesGlobalRoleGrant(GlobalRoleType.ROLE_USER, PermissionType.MOSQUE_MANAGE_MEMBERS)).isFalse();
        assertThat(PermissionRegistry.doesGlobalRoleGrant(GlobalRoleType.ROLE_USER, PermissionType.DONATION_VERIFY)).isFalse();
    }

    @Test
    @DisplayName("IMAM membership role should grant mosque management and verification permissions")
    void imamShouldHaveLeadershipPermissions() {
        Set<PermissionType> perms = PermissionRegistry.getPermissionsForMembershipRole(MembershipRole.IMAM);

        assertThat(perms).contains(
                PermissionType.MOSQUE_VIEW,
                PermissionType.MOSQUE_UPDATE,
                PermissionType.MOSQUE_MANAGE_MEMBERS,
                PermissionType.MOSQUE_VERIFY,
                PermissionType.DONATION_MANAGE,
                PermissionType.DONATION_VERIFY,
                PermissionType.INITIATIVE_APPROVE,
                PermissionType.PROJECT_MANAGE,
                PermissionType.VOLUNTEER_MANAGE,
                PermissionType.IMPACT_VERIFY
        );
    }

    @Test
    @DisplayName("TREASURER membership role should only grant financial and donation management")
    void treasurerShouldHaveFocusedPermissions() {
        assertThat(PermissionRegistry.doesMembershipRoleGrant(MembershipRole.TREASURER, PermissionType.DONATION_MANAGE)).isTrue();
        assertThat(PermissionRegistry.doesMembershipRoleGrant(MembershipRole.TREASURER, PermissionType.DONATION_VERIFY)).isTrue();

        assertThat(PermissionRegistry.doesMembershipRoleGrant(MembershipRole.TREASURER, PermissionType.MOSQUE_UPDATE)).isFalse();
        assertThat(PermissionRegistry.doesMembershipRoleGrant(MembershipRole.TREASURER, PermissionType.MOSQUE_MANAGE_MEMBERS)).isFalse();
    }

    @Test
    @DisplayName("Null roles and permissions should safely evaluate to false without exceptions")
    void nullHandling() {
        assertThat(PermissionRegistry.doesGlobalRoleGrant(null, PermissionType.MOSQUE_VIEW)).isFalse();
        assertThat(PermissionRegistry.doesGlobalRoleGrant(GlobalRoleType.ROLE_USER, null)).isFalse();
        assertThat(PermissionRegistry.doesMembershipRoleGrant(null, PermissionType.MOSQUE_VIEW)).isFalse();
        assertThat(PermissionRegistry.doesMembershipRoleGrant(MembershipRole.IMAM, null)).isFalse();
    }
}
