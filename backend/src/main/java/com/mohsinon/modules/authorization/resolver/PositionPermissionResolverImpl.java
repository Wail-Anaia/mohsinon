package com.mohsinon.modules.authorization.resolver;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.mohsinon.modules.authorization.entity.Permission;
import com.mohsinon.modules.authorization.entity.PositionPermission;
import com.mohsinon.modules.authorization.repository.PositionPermissionRepository;
import com.mohsinon.modules.mosques.entity.MosqueMembership;
import com.mohsinon.modules.users.entity.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PositionPermissionResolverImpl
        implements PositionPermissionResolver {

    private final PositionPermissionRepository positionPermissionRepository;

    @Override
    public Set<String> resolve(
            User user,
            MosqueMembership membership) {

        return positionPermissionRepository
                .findByPosition(membership.getPosition())
                .stream()
                .map(PositionPermission::getPermission)
                .map(Permission::getCode)
                .collect(Collectors.toSet());
    }

}