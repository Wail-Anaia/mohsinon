package com.mohsinon.core.security.authorization.model;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Static, in-memory registry defining the immutable mappings between:
 * 1. Global Roles -> Granular Permissions
 * 2. Membership Roles -> Granular Permissions
 *
 * Guarantees zero SQL joins for permission resolution and compile-time type safety.
 */
public final class PermissionRegistry {

    private static final Map<GlobalRoleType, Set<PermissionType>> GLOBAL_ROLE_PERMISSIONS;
    private static final Map<MembershipRole, Set<PermissionType>> MEMBERSHIP_ROLE_PERMISSIONS;

    static {
        // --- 1. Global Role Permissions ---
        Map<GlobalRoleType, Set<PermissionType>> globalMap = new EnumMap<>(GlobalRoleType.class);

        globalMap.put(GlobalRoleType.ROLE_USER, Collections.unmodifiableSet(EnumSet.of(
                PermissionType.MOSQUE_VIEW,
                PermissionType.DONATION_CREATE,
                PermissionType.INITIATIVE_CREATE
        )));

        globalMap.put(GlobalRoleType.ROLE_VOLUNTEER, Collections.unmodifiableSet(EnumSet.of(
                PermissionType.MOSQUE_VIEW,
                PermissionType.DONATION_CREATE,
                PermissionType.INITIATIVE_CREATE,
                PermissionType.VOLUNTEER_MANAGE
        )));

        globalMap.put(GlobalRoleType.ROLE_DONOR, Collections.unmodifiableSet(EnumSet.of(
                PermissionType.MOSQUE_VIEW,
                PermissionType.DONATION_CREATE,
                PermissionType.DONATION_MANAGE,
                PermissionType.INITIATIVE_CREATE
        )));

        globalMap.put(GlobalRoleType.ROLE_ADMIN, Collections.unmodifiableSet(EnumSet.allOf(PermissionType.class)));

        GLOBAL_ROLE_PERMISSIONS = Collections.unmodifiableMap(globalMap);

        // --- 2. Contextual Membership Role Permissions ---
        Map<MembershipRole, Set<PermissionType>> membershipMap = new EnumMap<>(MembershipRole.class);

        membershipMap.put(MembershipRole.IMAM, Collections.unmodifiableSet(EnumSet.of(
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
        )));

        membershipMap.put(MembershipRole.MOSQUE_PRESIDENT, Collections.unmodifiableSet(EnumSet.of(
                PermissionType.MOSQUE_VIEW,
                PermissionType.MOSQUE_UPDATE,
                PermissionType.MOSQUE_MANAGE_MEMBERS,
                PermissionType.DONATION_MANAGE,
                PermissionType.DONATION_VERIFY,
                PermissionType.INITIATIVE_APPROVE,
                PermissionType.PROJECT_MANAGE,
                PermissionType.VOLUNTEER_MANAGE
        )));

        membershipMap.put(MembershipRole.MOSQUE_COMMITTEE_MEMBER, Collections.unmodifiableSet(EnumSet.of(
                PermissionType.MOSQUE_VIEW,
                PermissionType.MOSQUE_UPDATE,
                PermissionType.DONATION_MANAGE,
                PermissionType.VOLUNTEER_MANAGE
        )));

        membershipMap.put(MembershipRole.TREASURER, Collections.unmodifiableSet(EnumSet.of(
                PermissionType.MOSQUE_VIEW,
                PermissionType.DONATION_MANAGE,
                PermissionType.DONATION_VERIFY
        )));

        membershipMap.put(MembershipRole.DONATION_MANAGER, Collections.unmodifiableSet(EnumSet.of(
                PermissionType.MOSQUE_VIEW,
                PermissionType.DONATION_MANAGE
        )));

        membershipMap.put(MembershipRole.VOLUNTEER_COORDINATOR, Collections.unmodifiableSet(EnumSet.of(
                PermissionType.MOSQUE_VIEW,
                PermissionType.VOLUNTEER_MANAGE
        )));

        membershipMap.put(MembershipRole.LOCAL_MODERATOR, Collections.unmodifiableSet(EnumSet.of(
                PermissionType.MOSQUE_VIEW,
                PermissionType.MOSQUE_UPDATE,
                PermissionType.INITIATIVE_APPROVE
        )));

        MEMBERSHIP_ROLE_PERMISSIONS = Collections.unmodifiableMap(membershipMap);
    }

    private PermissionRegistry() {
        // Utility class
    }

    public static Set<PermissionType> getPermissionsForGlobalRole(GlobalRoleType role) {
        if (role == null) {
            return Collections.emptySet();
        }
        return GLOBAL_ROLE_PERMISSIONS.getOrDefault(role, Collections.emptySet());
    }

    public static Set<PermissionType> getPermissionsForMembershipRole(MembershipRole role) {
        if (role == null) {
            return Collections.emptySet();
        }
        return MEMBERSHIP_ROLE_PERMISSIONS.getOrDefault(role, Collections.emptySet());
    }

    public static boolean doesGlobalRoleGrant(GlobalRoleType role, PermissionType permission) {
        if (role == null || permission == null) {
            return false;
        }
        if (role == GlobalRoleType.ROLE_ADMIN) {
            return true;
        }
        Set<PermissionType> permissions = GLOBAL_ROLE_PERMISSIONS.get(role);
        return permissions != null && permissions.contains(permission);
    }

    public static boolean doesMembershipRoleGrant(MembershipRole role, PermissionType permission) {
        if (role == null || permission == null) {
            return false;
        }
        Set<PermissionType> permissions = MEMBERSHIP_ROLE_PERMISSIONS.get(role);
        return permissions != null && permissions.contains(permission);
    }
}
