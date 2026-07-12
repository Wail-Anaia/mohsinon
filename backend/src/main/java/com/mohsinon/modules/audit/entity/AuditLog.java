package com.mohsinon.modules.audit.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.mohsinon.modules.audit.model.AuditAction;
import com.mohsinon.modules.audit.model.AuditEntityType;
import com.mohsinon.modules.users.entity.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
public class AuditLog {

    @Id
    @GeneratedValue
    private UUID id;

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

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}