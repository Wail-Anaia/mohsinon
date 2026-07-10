package com.mohsinon.modules.mosques.authorization ;

import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.mohsinon.modules.authorization.entity.Permission;
import com.mohsinon.modules.authorization.entity.PositionPermission;
import com.mohsinon.modules.authorization.entity.UserPermission;
import com.mohsinon.modules.authorization.repository.PositionPermissionRepository;
import com.mohsinon.modules.authorization.repository.UserPermissionRepository;
import com.mohsinon.modules.mosques.entity.Mosque;
import com.mohsinon.modules.mosques.entity.MosqueMembership;
import com.mohsinon.modules.mosques.repository.MosqueMembershipRepository;
import com.mohsinon.modules.mosques.repository.MosqueRepository;
import com.mohsinon.modules.users.entity.User;
import com.mohsinon.security.authorization.AuthorizationProvider;

@Component
public class MosqueAuthorizationProvider implements AuthorizationProvider {

    private final MosqueRepository mosqueRepository;
    private final MosqueMembershipRepository membershipRepository;
    private final UserPermissionRepository userPermissionRepository;
    private final PositionPermissionRepository positionPermissionRepository;

    public MosqueAuthorizationProvider(
            MosqueRepository mosqueRepository,
            MosqueMembershipRepository membershipRepository,
            UserPermissionRepository userPermissionRepository,
            PositionPermissionRepository positionPermissionRepository) {

        this.mosqueRepository = mosqueRepository;
        this.membershipRepository = membershipRepository;
        this.userPermissionRepository = userPermissionRepository;
        this.positionPermissionRepository = positionPermissionRepository;
    }

    @Override
    public String getGroupCode() {
        return "mosque";
    }

    @Override
    public void checkPermission(
            User user,
            UUID mosqueId,
            String permissionCode) {

        Mosque mosque = mosqueRepository.findById(mosqueId)
                .orElseThrow(() ->
                        new AccessDeniedException("Mosque not found"));

        // صلاحيات مباشرة للمستخدم
        boolean hasDirectPermission =
                userPermissionRepository.findByUser(user)
                        .stream()
                        .map(UserPermission::getPermission)
                        .map(Permission::getCode)
                        .anyMatch(permissionCode::equals);

        if (hasDirectPermission) {
            return;
        }

        // عضوية المسجد
        MosqueMembership membership =
                membershipRepository
                        .findByMosqueAndUserAndActiveTrue(mosque, user)
                        .orElseThrow(() ->
                                new AccessDeniedException("Access denied"));

        // صلاحيات المنصب
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