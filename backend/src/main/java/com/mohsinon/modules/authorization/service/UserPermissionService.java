package com.mohsinon.modules.authorization.service;

import java.util.List;
import java.util.UUID;

import com.mohsinon.modules.authorization.dto.request.UserPermissionRequest;
import com.mohsinon.modules.authorization.dto.response.UserPermissionResponse;

public interface UserPermissionService {

    List<UserPermissionResponse> findByUser(UUID userId);

    UserPermissionResponse assignPermission(
            UUID userId,
            UserPermissionRequest request);

    void removePermission(
            UUID userId,
            Long permissionId);

}