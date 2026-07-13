package com.mohsinon.modules.donations.audit.provider;

import org.springframework.stereotype.Component;

import com.mohsinon.modules.audit.model.AuditAction;
import com.mohsinon.modules.audit.model.AuditEntityType;
import com.mohsinon.modules.audit.provider.AuditDescriptionProvider;
import com.mohsinon.modules.donations.dto.response.DonationResponse;

@Component
public class DonationCreatedAuditProvider
        implements AuditDescriptionProvider {

    @Override
    public AuditEntityType getEntityType() {
        return AuditEntityType.DONATION;
    }

    @Override
    public AuditAction getAction() {
        return AuditAction.CREATE;
    }

    @Override
    public String buildDescription(
            Object[] args,
            Object result) {

        DonationResponse donation =
                (DonationResponse) result;

        return "Created donation: "
                + donation.getTitle();
    }
}