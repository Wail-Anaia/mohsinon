package com.mohsinon.core.security.authorization.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    @DisplayName("ROLE_VOLUNTEER should grant volunteer management in addition to user permissions")
    void volunteerRolePermissions() {
        assertThat(PermissionRegistry.doesGlobalRoleGrant(GlobalRoleType.ROLE_VOLUNTEER, PermissionType.VOLUNTEER_MANAGE)).isTrue();
        assertThat(PermissionRegistry.doesGlobalRoleGrant(GlobalRoleType.ROLE_VOLUNTEER, PermissionType.DONATION_CREATE)).isTrue();
        assertThat(PermissionRegistry.doesGlobalRoleGrant(GlobalRoleType.ROLE_VOLUNTEER, PermissionType.MOSQUE_UPDATE)).isFalse();
    }

    @Test
    @DisplayName("ROLE_DONOR should grant donation manage and create in addition to user permissions")
    void donorRolePermissions() {
        assertThat(PermissionRegistry.doesGlobalRoleGrant(GlobalRoleType.ROLE_DONOR, PermissionType.DONATION_CREATE)).isTrue();
        assertThat(PermissionRegistry.doesGlobalRoleGrant(GlobalRoleType.ROLE_DONOR, PermissionType.DONATION_MANAGE)).isTrue();
        assertThat(PermissionRegistry.doesGlobalRoleGrant(GlobalRoleType.ROLE_DONOR, PermissionType.DONATION_VERIFY)).isFalse();
        assertThat(PermissionRegistry.doesGlobalRoleGrant(GlobalRoleType.ROLE_DONOR, PermissionType.MOSQUE_UPDATE)).isFalse();
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
    @DisplayName("MOSQUE_PRESIDENT membership role should grant administrative rights without impact verify")
    void presidentShouldHaveGovernancePermissions() {
        Set<PermissionType> perms = PermissionRegistry.getPermissionsForMembershipRole(MembershipRole.MOSQUE_PRESIDENT);

        assertThat(perms).contains(
                PermissionType.MOSQUE_VIEW,
                PermissionType.MOSQUE_UPDATE,
                PermissionType.MOSQUE_MANAGE_MEMBERS,
                PermissionType.DONATION_MANAGE,
                PermissionType.DONATION_VERIFY,
                PermissionType.INITIATIVE_APPROVE,
                PermissionType.PROJECT_MANAGE,
                PermissionType.VOLUNTEER_MANAGE
        );
        assertThat(perms).doesNotContain(PermissionType.IMPACT_VERIFY);
    }

    @Test
    @DisplayName("TREASURER membership role should only grant financial and donation management")
    void treasurerShouldHaveFocusedPermissions() {
        assertThat(PermissionRegistry.doesMembershipRoleGrant(MembershipRole.TREASURER, PermissionType.DONATION_MANAGE)).isTrue();
        assertThat(PermissionRegistry.doesMembershipRoleGrant(MembershipRole.TREASURER, PermissionType.DONATION_VERIFY)).isTrue();

        assertThat(PermissionRegistry.doesMembershipRoleGrant(MembershipRole.TREASURER, PermissionType.MOSQUE_UPDATE)).isFalse();
        assertThat(PermissionRegistry.doesMembershipRoleGrant(MembershipRole.TREASURER, PermissionType.MOSQUE_MANAGE_MEMBERS)).isFalse();
        assertThat(PermissionRegistry.doesMembershipRoleGrant(MembershipRole.TREASURER, PermissionType.VOLUNTEER_MANAGE)).isFalse();
    }

    @Test
    @DisplayName("DONATION_MANAGER and VOLUNTEER_COORDINATOR should have strictly segregated capabilities")
    void segregatedRoles() {
        // Donation manager
        assertThat(PermissionRegistry.doesMembershipRoleGrant(MembershipRole.DONATION_MANAGER, PermissionType.DONATION_MANAGE)).isTrue();
        assertThat(PermissionRegistry.doesMembershipRoleGrant(MembershipRole.DONATION_MANAGER, PermissionType.VOLUNTEER_MANAGE)).isFalse();
        assertThat(PermissionRegistry.doesMembershipRoleGrant(MembershipRole.DONATION_MANAGER, PermissionType.DONATION_VERIFY)).isFalse();

        // Volunteer coordinator
        assertThat(PermissionRegistry.doesMembershipRoleGrant(MembershipRole.VOLUNTEER_COORDINATOR, PermissionType.VOLUNTEER_MANAGE)).isTrue();
        assertThat(PermissionRegistry.doesMembershipRoleGrant(MembershipRole.VOLUNTEER_COORDINATOR, PermissionType.DONATION_MANAGE)).isFalse();

        // Local moderator
        assertThat(PermissionRegistry.doesMembershipRoleGrant(MembershipRole.LOCAL_MODERATOR, PermissionType.INITIATIVE_APPROVE)).isTrue();
        assertThat(PermissionRegistry.doesMembershipRoleGrant(MembershipRole.LOCAL_MODERATOR, PermissionType.DONATION_VERIFY)).isFalse();
    }

    @Test
    @DisplayName("Returned permission sets should be immutable")
    void setsShouldBeImmutable() {
        Set<PermissionType> userPerms = PermissionRegistry.getPermissionsForGlobalRole(GlobalRoleType.ROLE_USER);
        assertThatThrownBy(() -> userPerms.add(PermissionType.ADMIN_ALL))
                .isInstanceOf(UnsupportedOperationException.class);

        Set<PermissionType> imamPerms = PermissionRegistry.getPermissionsForMembershipRole(MembershipRole.IMAM);
        assertThatThrownBy(() -> imamPerms.add(PermissionType.ADMIN_ALL))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("Null roles and permissions should safely evaluate to false without exceptions")
    void nullHandling() {
        assertThat(PermissionRegistry.doesGlobalRoleGrant(null, PermissionType.MOSQUE_VIEW)).isFalse();
        assertThat(PermissionRegistry.doesGlobalRoleGrant(GlobalRoleType.ROLE_USER, null)).isFalse();
        assertThat(PermissionRegistry.doesMembershipRoleGrant(null, PermissionType.MOSQUE_VIEW)).isFalse();
        assertThat(PermissionRegistry.doesMembershipRoleGrant(MembershipRole.IMAM, null)).isFalse();
        assertThat(PermissionRegistry.getPermissionsForGlobalRole(null)).isEmpty();
        assertThat(PermissionRegistry.getPermissionsForMembershipRole(null)).isEmpty();
    }
}
