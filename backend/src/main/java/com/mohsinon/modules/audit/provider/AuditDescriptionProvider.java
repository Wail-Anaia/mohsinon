package com.mohsinon.modules.audit.provider;

import com.mohsinon.modules.audit.model.AuditAction;
import com.mohsinon.modules.audit.model.AuditEntityType;

public interface AuditDescriptionProvider {

    AuditEntityType getEntityType();

    AuditAction getAction();

    String buildDescription(
            Object[] args,
            Object result
    );

}