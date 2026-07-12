package com.mohsinon.modules.mosques.service;

import com.mohsinon.modules.mosques.constants.MosquePositionCodes;
import com.mohsinon.modules.mosques.dto.response.MosqueDashboardResponse;
import com.mohsinon.modules.mosques.entity.Mosque;
import com.mohsinon.modules.mosques.entity.MosqueMembership;
import com.mohsinon.modules.mosques.exception.MosqueNotFoundException;
import com.mohsinon.modules.mosques.membership.model.MembershipStatus;
import com.mohsinon.modules.mosques.repository.MosqueMembershipRepository;
import com.mohsinon.modules.mosques.repository.MosquePositionRepository;
import com.mohsinon.modules.mosques.repository.MosqueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MosqueStatisticsService {

    private final MosqueRepository mosqueRepository;
    private final MosqueMembershipRepository membershipRepository;
    private final MosquePositionRepository positionRepository;

    public MosqueStatisticsService(
            MosqueRepository mosqueRepository,
            MosqueMembershipRepository membershipRepository,
            MosquePositionRepository positionRepository) {

        this.mosqueRepository = mosqueRepository;
        this.membershipRepository = membershipRepository;
        this.positionRepository = positionRepository;
    }

    public MosqueDashboardResponse getDashboard(java.util.UUID mosqueId) {

        Mosque mosque = mosqueRepository.findById(mosqueId)
                .orElseThrow(MosqueNotFoundException::new);

        MosqueDashboardResponse response =
                new MosqueDashboardResponse();

        response.setMosqueId(mosque.getId());
        response.setMosqueName(mosque.getName());

        response.setTotalMembers(
                membershipRepository.countByMosque(mosque));

        response.setActiveMembers(
                membershipRepository.countByMosqueAndStatus(
                        mosque,
                        MembershipStatus.ACTIVE));

        response.setSuspendedMembers(
                membershipRepository.countByMosqueAndStatus(
                        mosque,
                        MembershipStatus.SUSPENDED));

        response.setTerminatedMembers(
                membershipRepository.countByMosqueAndStatus(
                        mosque,
                        MembershipStatus.TERMINATED));

        long totalPositions =
                positionRepository.count();

        response.setTotalPositions(totalPositions);

        long occupiedPositions =
                membershipRepository.findByMosque(mosque)
                        .stream()
                        .filter(MosqueMembership::isActive)
                        .map(MosqueMembership::getPosition)
                        .distinct()
                        .count();

        response.setOccupiedPositions(occupiedPositions);

        response.setVacantPositions(
                totalPositions - occupiedPositions);

        membershipRepository
                .findByMosqueAndPosition_CodeAndStatus(
                        mosque,
                        MosquePositionCodes.IMAM,
                        MembershipStatus.ACTIVE)
                .ifPresent(imam ->

                        response.setCurrentImam(

                                imam.getUser().getFirstName()
                                        + " "
                                        + imam.getUser().getLastName()));

        return response;
    }
}