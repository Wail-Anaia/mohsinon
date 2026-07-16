package com.mohsinon.modules.authorization.service;

import java.util.List;

import com.mohsinon.modules.authorization.dto.request.PositionPermissionRequest;
import com.mohsinon.modules.authorization.dto.response.PositionPermissionResponse;

public interface PositionPermissionService {

    List<PositionPermissionResponse> getPermissions(Long positionId);

    PositionPermissionResponse assignPermission(
            Long positionId,
            PositionPermissionRequest request);

    void removePermission(
            Long positionId,
            Long permissionId);

}