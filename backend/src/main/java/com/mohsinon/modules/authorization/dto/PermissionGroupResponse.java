package com.mohsinon.modules.authorization.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PermissionGroupResponse {

    private Long id;

    private String code;

    private String name;

    private String description;
    
    private Boolean active;
}