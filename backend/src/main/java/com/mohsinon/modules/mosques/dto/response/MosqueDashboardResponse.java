package com.mohsinon.modules.mosques.dto.response;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MosqueDashboardResponse {

    private UUID mosqueId;

    private String mosqueName;

    private long totalMembers;

    private long activeMembers;

    private long suspendedMembers;

    private long terminatedMembers;

    private long totalPositions;

    private long occupiedPositions;

    private long vacantPositions;

    private String currentImam;

}