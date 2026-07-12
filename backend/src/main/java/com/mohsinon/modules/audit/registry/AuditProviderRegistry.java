package com.mohsinon.modules.audit.registry;

import java.util.List;

import org.springframework.stereotype.Component;

import com.mohsinon.modules.audit.model.AuditAction;
import com.mohsinon.modules.audit.model.AuditEntityType;
import com.mohsinon.modules.audit.provider.AuditDescriptionProvider;

@Component
public class AuditProviderRegistry {

    private final List<AuditDescriptionProvider> providers;

    public AuditProviderRegistry(
            List<AuditDescriptionProvider> providers) {

        this.providers = providers;
    }

    public AuditDescriptionProvider getProvider(
            AuditEntityType entityType,
            AuditAction action) {

        return providers.stream()

                .filter(provider ->

                        provider.getEntityType() == entityType

                        &&

                        provider.getAction() == action)

                .findFirst()

                .orElseThrow(() ->

                        new IllegalArgumentException(

                                "No Audit Provider registered for "

                                        + entityType

                                        + " / "

                                        + action));
    }

}