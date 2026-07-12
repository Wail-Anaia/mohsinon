package com.mohsinon.modules.authorization.resolver;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.mohsinon.modules.authorization.entity.Permission;
import com.mohsinon.modules.authorization.entity.UserPermission;
import com.mohsinon.modules.authorization.repository.UserPermissionRepository;
import com.mohsinon.modules.mosques.entity.MosqueMembership;
import com.mohsinon.modules.users.entity.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DirectPermissionResolverImpl
        implements DirectPermissionResolver {

    private final UserPermissionRepository userPermissionRepository;

    @Override
    public Set<String> resolve(
            User user,
            MosqueMembership membership) {

        return userPermissionRepository.findByUser(user)
                .stream()
                .filter(UserPermission::getGranted)
                .map(UserPermission::getPermission)
                .map(Permission::getCode)
                .collect(Collectors.toSet());
    }

}