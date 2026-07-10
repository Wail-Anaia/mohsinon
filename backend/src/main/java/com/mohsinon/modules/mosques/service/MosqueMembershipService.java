package com.mohsinon.modules.mosques.service;

import com.mohsinon.exception.MosqueMembershipAlreadyExistsException;
import com.mohsinon.exception.MosquePositionAlreadyAssignedException;
import com.mohsinon.exception.MosquePositionNotFoundException;
import com.mohsinon.modules.mosques.entity.Mosque;
import com.mohsinon.modules.mosques.entity.MosqueMembership;
import com.mohsinon.modules.mosques.entity.MosquePosition;
import com.mohsinon.modules.mosques.repository.MosqueMembershipRepository;
import com.mohsinon.modules.mosques.repository.MosquePositionRepository;
import com.mohsinon.modules.mosques.repository.MosqueRepository;
import com.mohsinon.modules.authorization.service.AuthorizationService;
import com.mohsinon.modules.mosques.dto.response.MosqueMembershipResponse;
import com.mohsinon.modules.mosques.mapper.MosqueMembershipMapper;
import com.mohsinon.modules.users.entity.User;
import com.mohsinon.modules.users.repository.UserRepository;
import com.mohsinon.exception.MosqueNotFoundException;
import com.mohsinon.exception.UserNotFoundException;
import com.mohsinon.exception.MosqueMembershipNotFoundException;

import java.time.LocalDate;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MosqueMembershipService {

    private final MosqueMembershipRepository membershipRepository;
    private final MosquePositionRepository positionRepository;
    private final MosqueRepository mosqueRepository;
    private final UserRepository userRepository;
    private final AuthorizationService authorizationService;

    public MosqueMembershipService(
            MosqueRepository mosqueRepository,
            MosqueMembershipRepository membershipRepository,
            MosquePositionRepository positionRepository,
            UserRepository userRepository,
            AuthorizationService authorizationService) {

        this.mosqueRepository = mosqueRepository;
        this.membershipRepository = membershipRepository;
        this.positionRepository = positionRepository;
        this.userRepository = userRepository;
        this.authorizationService = authorizationService;
    } 
    
    @Transactional
    public MosqueMembershipResponse assignPosition(
            Mosque mosque,
            User user,
            String positionCode,
            User appointedBy,
            String notes) {

        authorizationService.checkPermission(
                appointedBy,
                mosque,
                "mosque.add_member"
        );

        MosquePosition position = getPositionByCode(positionCode);

        MosqueMembership membership = createMembership(
                mosque,
                user,
                position,
                appointedBy,
                notes
        );

        return MosqueMembershipMapper.toResponse(membership);
    }
    
    @Transactional
    public MosqueMembershipResponse assignPosition(
            UUID mosqueId,
            UUID userId,
            String positionCode,
            User appointedBy,
            String notes) {

        Mosque mosque = mosqueRepository.findById(mosqueId)
                .orElseThrow(MosqueNotFoundException::new);

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return assignPosition(
                mosque,
                user,
                positionCode,
                appointedBy,
                notes
        );
    }

    private MosquePosition getPositionByCode(String code) {

        return positionRepository.findByCode(code)
                .orElseThrow(() -> new MosquePositionNotFoundException(code));
    }

    private void validateUserMembership(Mosque mosque, User user) {

        if (membershipRepository.existsByMosqueAndUserAndActiveTrue(mosque, user)) {
            throw new MosqueMembershipAlreadyExistsException();
        }
    }
    
    private MosqueMembership createMembership(
            Mosque mosque,
            User user,
            MosquePosition position,
            User appointedBy,
            String notes) {

        validateUserMembership(mosque, user);

        if (Boolean.TRUE.equals(position.getUniquePosition())) {
            validateUniquePosition(mosque, position);
        }

        MosqueMembership membership = new MosqueMembership();

        membership.setMosque(mosque);
        membership.setUser(user);
        membership.setPosition(position);
        membership.setAppointedBy(appointedBy);
        membership.setNotes(notes);
        membership.setActive(true);

        return membershipRepository.save(membership);
    }
    
    private void validateUniquePosition(Mosque mosque, MosquePosition position) {

        if (membershipRepository.existsByMosqueAndPositionAndActiveTrue(mosque, position)) {
            throw new MosquePositionAlreadyAssignedException(position.getName());
        }
    }
    
    public List<MosqueMembershipResponse> getActiveMembers(UUID mosqueId) {

        Mosque mosque = mosqueRepository.findById(mosqueId)
                .orElseThrow(MosqueNotFoundException::new);

        return membershipRepository
                .findAllByMosqueAndActiveTrue(mosque)
                .stream()
                .map(MosqueMembershipMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public MosqueMembershipResponse changeImam(
            UUID mosqueId,
            UUID newImamUserId,
            User appointedBy,
            String notes) {

        Mosque mosque = mosqueRepository.findById(mosqueId)
                .orElseThrow(MosqueNotFoundException::new);

        User newImam = userRepository.findById(newImamUserId)
                .orElseThrow(UserNotFoundException::new);

        authorizationService.checkPermission(
                appointedBy,
                mosque,
                "mosque.assign_imam"
        );

        membershipRepository
                .findByMosqueAndPosition_CodeAndActiveTrue(
                        mosque,
                        "IMAM")
                .ifPresent(currentImam -> {
                    currentImam.setActive(false);
                    currentImam.setEndDate(LocalDate.now());
                    membershipRepository.save(currentImam);
                });

        MosquePosition imamPosition = getPositionByCode("IMAM");

        MosqueMembership membership = createMembership(
                mosque,
                newImam,
                imamPosition,
                appointedBy,
                notes
        );

        return MosqueMembershipMapper.toResponse(membership);
    }
    
    @Transactional
    public MosqueMembershipResponse deactivateMembership(UUID membershipId, User currentUser) {
 
        MosqueMembership membership = membershipRepository
                .findByIdAndActiveTrue(membershipId)
                .orElseThrow(MosqueMembershipNotFoundException::new);
        
        authorizationService.checkPermission(
                currentUser,
                membership.getMosque(),
                "mosque.remove_member"
        );
        
        membership.setActive(false);
        membership.setEndDate(LocalDate.now());

        MosqueMembership saved =
                membershipRepository.save(membership);

        return MosqueMembershipMapper.toResponse(saved);
    }
    
    public MosqueMembershipResponse getCurrentImam(UUID mosqueId) {

        Mosque mosque = mosqueRepository.findById(mosqueId)
                .orElseThrow(MosqueNotFoundException::new);

        MosqueMembership imam = membershipRepository
                .findByMosqueAndPosition_CodeAndActiveTrue(
                        mosque,
                        "IMAM")
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new MosqueMembershipNotFoundException());

        return MosqueMembershipMapper.toResponse(imam);
    }
    
    public List<MosqueMembershipResponse> getImamHistory(UUID mosqueId) {

        Mosque mosque = mosqueRepository.findById(mosqueId)
                .orElseThrow(MosqueNotFoundException::new);

        return membershipRepository
                .findByMosqueAndPosition_CodeOrderByStartDateDesc(
                        mosque,
                        "IMAM")
                .stream()
                .map(MosqueMembershipMapper::toResponse)
                .toList();
    }
    
    public List<MosqueMembershipResponse> getUserMembershipHistory(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return membershipRepository
                .findByUserOrderByStartDateDesc(user)
                .stream()
                .map(MosqueMembershipMapper::toResponse)
                .toList();
    }

}