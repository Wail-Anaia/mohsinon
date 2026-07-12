package com.mohsinon.modules.authorization.mapper;

import org.springframework.stereotype.Component;

import com.mohsinon.modules.authorization.dto.PositionPermissionResponse;
import com.mohsinon.modules.authorization.entity.PositionPermission;

@Component
public class PositionPermissionMapper {

    public PositionPermissionResponse toResponse(
            PositionPermission entity) {

        return PositionPermissionResponse.builder()
                .id(entity.getId())
                .permissionId(entity.getPermission().getId())
                .permissionCode(entity.getPermission().getCode())
                .permissionName(entity.getPermission().getName())
                .build();
    }

}