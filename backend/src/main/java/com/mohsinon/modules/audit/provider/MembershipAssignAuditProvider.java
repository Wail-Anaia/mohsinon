package com.mohsinon.modules.audit.provider;

import org.springframework.stereotype.Component;

import com.mohsinon.modules.audit.model.AuditAction;
import com.mohsinon.modules.audit.model.AuditEntityType;
import com.mohsinon.modules.mosques.dto.response.MosqueMembershipResponse;

@Component
public class MembershipAssignAuditProvider
        implements AuditDescriptionProvider {

    @Override
    public AuditEntityType getEntityType() {
        return AuditEntityType.MEMBERSHIP;
    }

    @Override
    public AuditAction getAction() {
        return AuditAction.ASSIGN;
    }

    @Override
    public String buildDescription(
            Object[] args,
            Object result) {

        MosqueMembershipResponse membership =
                (MosqueMembershipResponse) result;

        return "Assigned "

                + membership.getFullName()

                + " as "

                + membership.getPositionName()

                + " in "

                + membership.getMosqueName();
    }

}