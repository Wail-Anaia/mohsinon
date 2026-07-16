package com.mohsinon.modules.audit.entity;

import com.mohsinon.modules.audit.model.AuditAction;
import com.mohsinon.modules.audit.model.AuditEntityType;
import com.mohsinon.modules.users.entity.User;
import com.mohsinon.shared.entity.AuditableEntity;

import java.util.UUID;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
public class AuditLog extends AuditableEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    private User actor;

    @Enumerated(EnumType.STRING)
    private AuditAction action;

    @Enumerated(EnumType.STRING)
    private AuditEntityType entityType;

    private UUID entityId;
    
    @Column(length = 200)
    private String entityName;

    @Column(length = 1000)
    private String description;
}