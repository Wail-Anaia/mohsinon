package com.mohsinon.modules.audit.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.mohsinon.modules.audit.model.AuditAction;
import com.mohsinon.modules.audit.model.AuditEntityType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditLogResponse {

    private UUID id;

    private String actor;

    private AuditAction action;

    private AuditEntityType entityType;

    private UUID entityId;

    private String description;

    private LocalDateTime createdAt;

}