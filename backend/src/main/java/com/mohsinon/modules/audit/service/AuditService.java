package com.mohsinon.modules.audit.service;

import org.springframework.stereotype.Service;

import com.mohsinon.modules.audit.entity.AuditLog;
import com.mohsinon.modules.audit.model.AuditAction;
import com.mohsinon.modules.audit.model.AuditEntityType;
import com.mohsinon.modules.audit.model.AuditableResource;
import com.mohsinon.modules.audit.provider.AuditDescriptionProvider;
import com.mohsinon.modules.audit.registry.AuditProviderRegistry;
import com.mohsinon.modules.audit.repository.AuditLogRepository;
import com.mohsinon.modules.users.entity.User;

@Service
public class AuditService {

    private final AuditLogRepository repository;
    private final AuditProviderRegistry registry;

    public AuditService(
            AuditLogRepository repository,
            AuditProviderRegistry registry) {

        this.repository = repository;
        this.registry = registry;
    }

    public void log(
            User actor,
            AuditEntityType entityType,
            AuditAction action,
            Object[] args,
            Object result) {

        AuditLog log = new AuditLog();

        log.setActor(actor);
        log.setEntityType(entityType);
        log.setAction(action);

        if (result instanceof AuditableResource auditable) {

            log.setEntityId(auditable.getAuditEntityId());
            log.setEntityName(auditable.getAuditEntityName());
        }

        String description;

        try {

            AuditDescriptionProvider provider =
                    registry.getProvider(entityType, action);

            description =
                    provider.buildDescription(args, result);

        } catch (IllegalArgumentException ex) {

            description = action + " " + entityType;
        }

        log.setDescription(description);

        repository.save(log);
    }
}