package com.mohsinon.modules.mosques.dto.response;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MosqueDashboardResponse {
	
	private UUID mosqueId;

    private String mosqueName;

    /*
     * Membership Statistics
     */
    private long totalMembers;
    private long activeMembers;
    private long suspendedMembers;
    private long terminatedMembers;

    /*
     * Position Statistics
     */
    private long totalPositions;
    private long occupiedPositions;
    private long vacantPositions;
    private String currentImam;

    /*
     * Donation Statistics
     */
    private long totalDonations;
    private long pendingDonations;
    private long receivedDonations;
    private long allocatedDonations;
    private long deliveredDonations;
    private long cancelledDonations;

    public MosqueDashboardResponse() {
    	
//    	private MembershipStatistics membership;
//
//        private PositionStatistics positions;
//
//        private DonationStatistics donations;
//
//        private VolunteerStatistics volunteers;
//
//        private InventoryStatistics inventory;
    }
}