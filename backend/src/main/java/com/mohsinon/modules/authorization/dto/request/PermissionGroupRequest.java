package com.mohsinon.modules.authorization.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionGroupRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    private String description;
    
    private Boolean active = true;
}