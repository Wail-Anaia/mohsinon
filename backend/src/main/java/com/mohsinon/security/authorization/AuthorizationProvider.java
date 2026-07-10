package com.mohsinon.security.authorization;

import java.util.UUID;

import com.mohsinon.modules.users.entity.User;

public interface AuthorizationProvider {

    /**
     * permission_groups.code
     * مثال:
     * mosque
     * association
     * project
     */
    String getGroupCode();

    void checkPermission(
            User user,
            UUID resourceId,
            String permission);
}