package com.mohsinon.modules.audit.model;

import java.util.UUID;

public interface AuditableResource {

    UUID getAuditEntityId();

    default String getAuditEntityName() {
        return null;
    }

}