package com.mohsinon.modules.mosques.service;

import com.mohsinon.modules.mosques.exception.MosqueMembershipAlreadyExistsException;
import com.mohsinon.modules.mosques.exception.MosquePositionAlreadyAssignedException;
import com.mohsinon.modules.mosques.exception.MosquePositionNotFoundException;
import com.mohsinon.modules.mosques.entity.Mosque;
import com.mohsinon.modules.mosques.entity.MosqueMembership;
import com.mohsinon.modules.mosques.entity.MosquePosition;
import com.mohsinon.modules.mosques.repository.MosqueMembershipRepository;
import com.mohsinon.modules.mosques.repository.MosquePositionRepository;
import com.mohsinon.modules.mosques.repository.MosqueRepository;
import com.mohsinon.modules.audit.annotation.Audit;
import com.mohsinon.modules.audit.model.*;
import com.mohsinon.modules.mosques.constants.MosquePositionCodes;
import com.mohsinon.modules.mosques.dto.response.MosqueMembershipResponse;
import com.mohsinon.modules.mosques.mapper.MosqueMembershipMapper;
import com.mohsinon.modules.mosques.membership.model.MembershipStatus;
import com.mohsinon.modules.users.entity.User;
import com.mohsinon.modules.users.repository.UserRepository;
import com.mohsinon.modules.mosques.exception.MosqueNotFoundException;
import com.mohsinon.modules.users.exception.UserNotFoundException;
import com.mohsinon.modules.mosques.exception.MosqueMembershipNotFoundException;
import com.mohsinon.security.annotation.RequirePermission;
import com.mohsinon.security.annotation.ResourceId;
import com.mohsinon.security.current.CurrentUserService;

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
    private final CurrentUserService currentUserService;

    public MosqueMembershipService(
            MosqueRepository mosqueRepository,
            MosqueMembershipRepository membershipRepository,
            MosquePositionRepository positionRepository,
            UserRepository userRepository,
            CurrentUserService currentUserService) {

        this.mosqueRepository = mosqueRepository;
        this.membershipRepository = membershipRepository;
        this.positionRepository = positionRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }
    
    
    private MosqueMembershipResponse assignPositionInternal(
            Mosque mosque,
            User user,
            String positionCode,
            User currentUser,
            String notes) {

        MosquePosition position = getPositionByCode(positionCode);

        MosqueMembership membership = createMembership(
                mosque,
                user,
                position,
                currentUser,
                notes
        );

        return MosqueMembershipMapper.toResponse(membership);
    }
    
    @Audit(action = AuditAction.ASSIGN, entity = AuditEntityType.MEMBERSHIP)
    @RequirePermission(groupCode = "mosque", permission = "add_member")
    @Transactional
    public MosqueMembershipResponse assignPosition(
            @ResourceId UUID mosqueId,
            UUID userId,
            String positionCode,
            String notes) {

        User currentUser = currentUserService.getCurrentUser();

        Mosque mosque = mosqueRepository.findById(mosqueId)
                .orElseThrow(MosqueNotFoundException::new);

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return assignPositionInternal(
                mosque,
                user,
                positionCode,
                currentUser,
                notes);
    }

    private MosquePosition getPositionByCode(String code) {

        return positionRepository.findByCode(code)
                .orElseThrow(() -> new MosquePositionNotFoundException(code));
    }

    private void validateUserMembership(Mosque mosque, User user) {

    	if (membershipRepository.existsByMosqueAndUserAndStatus(
    	        mosque,
    	        user,
    	        MembershipStatus.ACTIVE)) {

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
        membership.activate();

        return membershipRepository.save(membership);
    }
    
    private void validateUniquePosition(Mosque mosque, MosquePosition position) {

    	if (membershipRepository.existsByMosqueAndPositionAndStatus(
    	        mosque,
    	        position,
    	        MembershipStatus.ACTIVE)) {

    	    throw new MosquePositionAlreadyAssignedException(position.getName());
    	}
    }
    
    @Transactional
    public void createFounderMembership(
            Mosque mosque,
            User founder) {

        MosquePosition position =
                getPositionByCode(
                        MosquePositionCodes.COMMITTEE_PRESIDENT);

        createMembership(
                mosque,
                founder,
                position,
                founder,
                "Mosque creator");
    }
    
    public List<MosqueMembershipResponse> getActiveMembers(UUID mosqueId) {

        Mosque mosque = mosqueRepository.findById(mosqueId)
                .orElseThrow(MosqueNotFoundException::new);

        return membershipRepository
        		.findAllByMosqueAndStatus(
        		        mosque,
        		        MembershipStatus.ACTIVE
        		)
                .stream()
                .map(MosqueMembershipMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @RequirePermission(groupCode = "mosque", permission = "assign_imam")
    @Transactional
    public MosqueMembershipResponse changeImam(
    		@ResourceId UUID mosqueId,
            UUID newImamUserId,
            String notes) {
    	
    	User currentUser = currentUserService.getCurrentUser();

        Mosque mosque = mosqueRepository.findById(mosqueId)
                .orElseThrow(MosqueNotFoundException::new);

        User newImam = userRepository.findById(newImamUserId)
                .orElseThrow(UserNotFoundException::new);

        membershipRepository
		        .findByMosqueAndPosition_CodeAndStatus(
		                mosque,
		                MosquePositionCodes.IMAM,
		                MembershipStatus.ACTIVE
		        )
                .ifPresent(currentImam -> {
                	currentImam.terminate();
                    membershipRepository.save(currentImam);
                });

        MosquePosition imamPosition = getPositionByCode(MosquePositionCodes.IMAM);

        MosqueMembership membership = createMembership(
                mosque,
                newImam,
                imamPosition,
                currentUser,
                notes
        );

        return MosqueMembershipMapper.toResponse(membership);
    }
    
    @Audit(action = AuditAction.REMOVE, entity = AuditEntityType.MEMBERSHIP)
    @RequirePermission(groupCode = "mosque", permission = "remove_member")
    @Transactional
    public MosqueMembershipResponse terminateMembership(UUID membershipId) {
 
        MosqueMembership membership = membershipRepository
        		.findByIdAndStatus(
        		        membershipId,
        		        MembershipStatus.ACTIVE
        		)
                .orElseThrow(MosqueMembershipNotFoundException::new);
        
        membership.terminate();

        MosqueMembership saved =
                membershipRepository.save(membership);

        return MosqueMembershipMapper.toResponse(saved);
    }
    
    public MosqueMembershipResponse getCurrentImam(UUID mosqueId) {

        Mosque mosque = mosqueRepository.findById(mosqueId)
                .orElseThrow(MosqueNotFoundException::new);

        MosqueMembership imam = membershipRepository
                .findByMosqueAndPosition_CodeAndStatus(
                        mosque,
                        MosquePositionCodes.IMAM,
                        MembershipStatus.ACTIVE
                )
                .orElseThrow(MosqueMembershipNotFoundException::new);

        return MosqueMembershipMapper.toResponse(imam);
    }
    
    public List<MosqueMembershipResponse> getImamHistory(UUID mosqueId) {

        Mosque mosque = mosqueRepository.findById(mosqueId)
                .orElseThrow(MosqueNotFoundException::new);

        return membershipRepository
                .findByMosqueAndPosition_CodeOrderByStartDateDesc(
                        mosque,
                        MosquePositionCodes.IMAM)
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