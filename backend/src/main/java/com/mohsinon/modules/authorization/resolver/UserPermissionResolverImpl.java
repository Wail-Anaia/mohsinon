package com.mohsinon.modules.authorization.resolver;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserPermissionResolverImpl
        implements UserPermissionResolver {

    @Override
    public Set<String> resolve(UUID userId) {

        Set<String> permissions = new HashSet<>();

        // سنضيف هنا UserPermissionRepository
        // لاحقًا سنجلب الصلاحيات المباشرة للمستخدم

        return permissions;
    }

}