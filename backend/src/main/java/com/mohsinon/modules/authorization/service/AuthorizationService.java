package com.mohsinon.modules.authorization.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mohsinon.modules.users.entity.User;
import com.mohsinon.security.authorization.AuthorizationRegistry;

@Service
public class AuthorizationService {

    private final AuthorizationRegistry registry;

    public AuthorizationService(
            AuthorizationRegistry registry) {

        this.registry = registry;
    }

    public void checkPermission(
            User user,
            String groupCode,
            UUID resourceId,
            String permission) {

        registry
                .getProvider(groupCode)
                .checkPermission(
                        user,
                        resourceId,
                        permission);
    }

}