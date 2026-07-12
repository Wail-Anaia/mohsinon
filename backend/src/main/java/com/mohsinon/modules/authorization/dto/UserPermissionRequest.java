package com.mohsinon.modules.authorization.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPermissionRequest {

    @NotNull(message = "Permission id is required.")
    private Long permissionId;

    private Boolean granted = true;

}