package com.mohsinon.modules.authorization.mapper;

import org.springframework.stereotype.Component;

import com.mohsinon.modules.authorization.dto.response.UserPermissionResponse;
import com.mohsinon.modules.authorization.entity.UserPermission;

@Component
public class UserPermissionMapper {

    public UserPermissionResponse toResponse(
            UserPermission entity) {

        return UserPermissionResponse.builder()
                .id(entity.getId())
                .permissionId(entity.getPermission().getId())
                .permissionCode(entity.getPermission().getCode())
                .permissionName(entity.getPermission().getName())
                .granted(entity.getGranted())
                .build();
    }

}