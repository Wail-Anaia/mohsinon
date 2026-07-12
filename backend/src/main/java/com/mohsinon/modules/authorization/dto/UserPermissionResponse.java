package com.mohsinon.modules.authorization.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserPermissionResponse {

    private Long id;

    private Long permissionId;

    private String permissionCode;

    private String permissionName;

    private Boolean granted;

}