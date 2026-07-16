package com.mohsinon.modules.authorization.mapper;

import org.springframework.stereotype.Component;

import com.mohsinon.modules.authorization.dto.response.PermissionResponse;
import com.mohsinon.modules.authorization.entity.Permission;

@Component
public class PermissionMapper {

    public PermissionResponse toResponse(Permission entity) {

        return PermissionResponse.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .permissionGroupId(entity.getPermissionGroup().getId())
                .permissionGroupCode(entity.getPermissionGroup().getCode())
                .permissionGroupName(entity.getPermissionGroup().getName())
                .active(entity.getActive())
                .build();
    }

}