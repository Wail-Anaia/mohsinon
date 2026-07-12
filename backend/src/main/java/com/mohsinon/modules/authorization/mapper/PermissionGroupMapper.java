package com.mohsinon.modules.authorization.mapper;

import org.springframework.stereotype.Component;

import com.mohsinon.modules.authorization.dto.PermissionGroupResponse;
import com.mohsinon.modules.authorization.entity.PermissionGroup;

@Component
public class PermissionGroupMapper {

    public PermissionGroupResponse toResponse(PermissionGroup entity) {

        return PermissionGroupResponse.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .active(entity.getActive())
                .build();
    }
}