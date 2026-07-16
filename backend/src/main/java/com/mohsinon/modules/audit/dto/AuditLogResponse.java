package com.mohsinon.modules.audit.dto;

import java.util.UUID;

import com.mohsinon.modules.audit.model.AuditAction;
import com.mohsinon.modules.audit.model.AuditEntityType;
import com.mohsinon.shared.entity.AuditableEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditLogResponse extends AuditableEntity{

    private String actor;

    private AuditAction action;

    private AuditEntityType entityType;

    private UUID entityId;

    private String description;
}