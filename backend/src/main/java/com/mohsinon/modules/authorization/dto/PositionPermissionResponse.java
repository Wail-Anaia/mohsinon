package com.mohsinon.modules.authorization.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PositionPermissionResponse {

    private Long id;

    private Long permissionId;

    private String permissionCode;

    private String permissionName;

}