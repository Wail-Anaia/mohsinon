package com.mohsinon.modules.authorization.resolver;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.mohsinon.modules.authorization.cache.PermissionCache;
import com.mohsinon.modules.mosques.entity.MosqueMembership;
import com.mohsinon.modules.users.entity.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CompositePermissionResolver
        implements PermissionResolver {

    private static final Logger log =
            LoggerFactory.getLogger(
                    CompositePermissionResolver.class);

    private final PermissionCache permissionCache;

    private final DirectPermissionResolver directResolver;

    private final PositionPermissionResolver positionResolver;

    @Override
    public Set<String> resolve(
            User user,
            MosqueMembership membership) {

    	UUID userId = user.getId();

    	Set<String> permissions = permissionCache.get(userId);

        if (permissions != null) {

            log.debug(
                    "Permission cache HIT for user {}",
                    userId);

            return permissions;
        }

        log.debug(
                "Permission cache MISS for user {}",
                userId);

        permissions = new HashSet<>();

        permissions.addAll(
                directResolver.resolve(
                        user,
                        membership));

        permissions.addAll(
                positionResolver.resolve(
                        user,
                        membership));

        permissionCache.put(
                userId,
                permissions);

        return permissions;
    }

}