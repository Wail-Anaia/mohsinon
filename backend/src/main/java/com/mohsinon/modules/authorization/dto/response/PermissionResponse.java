package com.mohsinon.modules.authorization.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PermissionResponse {

    private Long id;

    private String code;

    private String name;

    private String description;

    private Long permissionGroupId;

    private String permissionGroupCode;

    private String permissionGroupName;

    private Boolean active;

}