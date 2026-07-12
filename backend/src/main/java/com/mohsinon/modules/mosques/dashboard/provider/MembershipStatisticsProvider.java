package com.mohsinon.modules.mosques.dashboard.provider;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.mohsinon.modules.mosques.dto.response.MosqueDashboardResponse;
import com.mohsinon.modules.mosques.entity.Mosque;
import com.mohsinon.modules.mosques.exception.MosqueNotFoundException;
import com.mohsinon.modules.mosques.membership.model.MembershipStatus;
import com.mohsinon.modules.mosques.repository.MosqueMembershipRepository;
import com.mohsinon.modules.mosques.repository.MosqueRepository;

@Component
public class MembershipStatisticsProvider
        implements MosqueDashboardProvider {

    private final MosqueRepository mosqueRepository;
    private final MosqueMembershipRepository membershipRepository;

    public MembershipStatisticsProvider(
            MosqueRepository mosqueRepository,
            MosqueMembershipRepository membershipRepository) {

        this.mosqueRepository = mosqueRepository;
        this.membershipRepository = membershipRepository;
    }
    
    @Override
    public String getCode() {
        return "membership";
    }

    @Override
    public void enrich(
            UUID mosqueId,
            MosqueDashboardResponse dashboard) {

        Mosque mosque = mosqueRepository.findById(mosqueId)
                .orElseThrow(MosqueNotFoundException::new);

        dashboard.setTotalMembers(
                membershipRepository.countByMosque(mosque));

        dashboard.setActiveMembers(
                membershipRepository.countByMosqueAndStatus(
                        mosque,
                        MembershipStatus.ACTIVE));

        dashboard.setSuspendedMembers(
                membershipRepository.countByMosqueAndStatus(
                        mosque,
                        MembershipStatus.SUSPENDED));

        dashboard.setTerminatedMembers(
                membershipRepository.countByMosqueAndStatus(
                        mosque,
                        MembershipStatus.TERMINATED));
    }
}