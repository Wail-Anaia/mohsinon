package com.mohsinon.modules.authorization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionRequest {

    @NotBlank(message = "Permission code is required.")
    private String code;

    @NotBlank(message = "Permission name is required.")
    private String name;

    private String description;

    @NotNull(message = "Permission group is required.")
    private Long permissionGroupId;

    private Boolean active = true;

}