package com.mohsinon.modules.authorization.service;

import com.mohsinon.modules.authorization.entity.Permission;
import com.mohsinon.modules.authorization.entity.PositionPermission;
import com.mohsinon.modules.authorization.entity.UserPermission;
import com.mohsinon.modules.authorization.repository.PositionPermissionRepository;
import com.mohsinon.modules.authorization.repository.UserPermissionRepository;
import com.mohsinon.modules.mosques.entity.Mosque;
import com.mohsinon.modules.mosques.entity.MosqueMembership;
import com.mohsinon.modules.mosques.repository.MosqueMembershipRepository;
import com.mohsinon.modules.users.entity.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final UserPermissionRepository userPermissionRepository;
    private final PositionPermissionRepository positionPermissionRepository;
    private final MosqueMembershipRepository membershipRepository;

    public AuthorizationService(
            UserPermissionRepository userPermissionRepository,
            PositionPermissionRepository positionPermissionRepository,
            MosqueMembershipRepository membershipRepository) {

        this.userPermissionRepository = userPermissionRepository;
        this.positionPermissionRepository = positionPermissionRepository;
        this.membershipRepository = membershipRepository;
    }

    public void checkPermission(
            User user,
            Mosque mosque,
            String permissionCode) {

        // 1) User Permissions
        boolean hasDirectPermission =
                userPermissionRepository.findByUser(user)
                        .stream()
                        .map(UserPermission::getPermission)
                        .map(Permission::getCode)
                        .anyMatch(permissionCode::equals);

        if (hasDirectPermission) {
            return;
        }

        // 2) Active Membership
        MosqueMembership membership =
                membershipRepository
                        .findByMosqueAndUserAndActiveTrue(mosque, user)
                        .orElseThrow(() ->
                                new AccessDeniedException("Access denied"));

        // 3) Position Permissions
        boolean hasPositionPermission =
                positionPermissionRepository
                        .findByPosition(membership.getPosition())
                        .stream()
                        .map(PositionPermission::getPermission)
                        .map(Permission::getCode)
                        .anyMatch(permissionCode::equals);

        if (!hasPositionPermission) {
            throw new AccessDeniedException("Access denied");
        }
    }

}