package com.mohsinon.modules.mosques.dashboard.provider;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.mohsinon.modules.mosques.constants.MosquePositionCodes;
import com.mohsinon.modules.mosques.dto.response.MosqueDashboardResponse;
import com.mohsinon.modules.mosques.entity.Mosque;
import com.mohsinon.modules.mosques.entity.MosqueMembership;
import com.mohsinon.modules.mosques.exception.MosqueNotFoundException;
import com.mohsinon.modules.mosques.membership.model.MembershipStatus;
import com.mohsinon.modules.mosques.repository.MosqueMembershipRepository;
import com.mohsinon.modules.mosques.repository.MosquePositionRepository;
import com.mohsinon.modules.mosques.repository.MosqueRepository;

@Component
public class PositionStatisticsProvider
        implements MosqueDashboardProvider {

    private final MosqueRepository mosqueRepository;
    private final MosqueMembershipRepository membershipRepository;
    private final MosquePositionRepository positionRepository;

    public PositionStatisticsProvider(
            MosqueRepository mosqueRepository,
            MosqueMembershipRepository membershipRepository,
            MosquePositionRepository positionRepository) {

        this.mosqueRepository = mosqueRepository;
        this.membershipRepository = membershipRepository;
        this.positionRepository = positionRepository;
    }

    @Override
    public String getCode() {
        return "position";
    }

    @Override
    public void enrich(
            UUID mosqueId,
            MosqueDashboardResponse dashboard) {

        Mosque mosque =
                mosqueRepository.findById(mosqueId)
                        .orElseThrow(MosqueNotFoundException::new);

        long totalPositions =
                positionRepository.count();

        dashboard.setTotalPositions(totalPositions);

        long occupied =
                membershipRepository
                        .findByMosqueAndStatus(
                                mosque,
                                MembershipStatus.ACTIVE)
                        .stream()
                        .map(MosqueMembership::getPosition)
                        .distinct()
                        .count();

        dashboard.setOccupiedPositions(occupied);

        dashboard.setVacantPositions(
                totalPositions - occupied);

        membershipRepository
                .findByMosqueAndPosition_CodeAndStatus(
                        mosque,
                        MosquePositionCodes.IMAM,
                        MembershipStatus.ACTIVE)
                .ifPresent(imam ->

                        dashboard.setCurrentImam(

                                imam.getUser().getFirstName()
                                        + " "
                                        + imam.getUser().getLastName()));
    }
}