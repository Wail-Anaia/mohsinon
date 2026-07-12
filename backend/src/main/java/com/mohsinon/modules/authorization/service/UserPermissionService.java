package com.mohsinon.modules.authorization.service;

import java.util.List;
import java.util.UUID;

import com.mohsinon.modules.authorization.dto.UserPermissionRequest;
import com.mohsinon.modules.authorization.dto.UserPermissionResponse;

public interface UserPermissionService {

    List<UserPermissionResponse> findByUser(UUID userId);

    UserPermissionResponse assignPermission(
            UUID userId,
            UserPermissionRequest request);

    void removePermission(
            UUID userId,
            Long permissionId);

}