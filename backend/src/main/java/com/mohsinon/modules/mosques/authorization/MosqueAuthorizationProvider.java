package com.mohsinon.modules.mosques.authorization;

import java.util.Set;
import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.mohsinon.modules.authorization.resolver.PermissionResolver;
import com.mohsinon.modules.mosques.entity.Mosque;
import com.mohsinon.modules.mosques.entity.MosqueMembership;
import com.mohsinon.modules.mosques.membership.model.MembershipStatus;
import com.mohsinon.modules.mosques.repository.MosqueMembershipRepository;
import com.mohsinon.modules.mosques.repository.MosqueRepository;
import com.mohsinon.modules.users.entity.User;
import com.mohsinon.security.authorization.AuthorizationProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MosqueAuthorizationProvider implements AuthorizationProvider {

    private final MosqueRepository mosqueRepository;
    private final MosqueMembershipRepository membershipRepository;
    private final PermissionResolver permissionResolver;

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
                        new AccessDeniedException("Mosque not found."));

        MosqueMembership membership =
                membershipRepository
                        .findByMosqueAndUserAndStatus(
                                mosque,
                                user,
                                MembershipStatus.ACTIVE
                        )
                        .orElseThrow(() ->
                                new AccessDeniedException("Access denied."));

        Set<String> permissions =
                permissionResolver.resolve(user, membership);

        if (!permissions.contains(permissionCode)) {
            throw new AccessDeniedException("Access denied.");
        }
    }
}