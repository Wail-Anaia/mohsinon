package com.mohsinon.modules.mosques.dto.response;

import java.time.LocalDate;
import java.util.UUID;

import com.mohsinon.modules.audit.model.AuditableResource;
import com.mohsinon.modules.mosques.membership.model.MembershipStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MosqueMembershipResponse
        implements AuditableResource {

    private UUID id;

    private UUID mosqueId;
    private String mosqueName;

    private UUID userId;
    private String fullName;

    private String positionCode;
    private String positionName;

    private LocalDate startDate;
    private LocalDate endDate;

    private MembershipStatus status;

    private UUID appointedById;
    private String appointedByName;

    private String notes;

    @Override
    public UUID getAuditEntityId() {
        return id;
    }
    
    @Override
    public String getAuditEntityName() {
        return fullName;
    }
}