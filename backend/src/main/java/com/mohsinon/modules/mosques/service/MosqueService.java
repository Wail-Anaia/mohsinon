package com.mohsinon.modules.mosques.service;

import com.mohsinon.modules.mosques.dto.request.CreateMosqueRequest;
import com.mohsinon.modules.mosques.dto.response.MosqueResponse;
import com.mohsinon.modules.mosques.entity.Mosque;
import com.mohsinon.modules.mosques.mapper.MosqueMapper;
import com.mohsinon.modules.mosques.repository.MosqueRepository;
import com.mohsinon.exception.MosqueNotFoundException;

import com.mohsinon.security.utils.SecurityUtils;
import com.mohsinon.modules.mosques.constants.MosquePositionCodes;
import com.mohsinon.modules.mosques.entity.MosqueMembership;
import com.mohsinon.modules.mosques.service.MosqueMembershipService;
import com.mohsinon.modules.users.entity.User;
import com.mohsinon.modules.users.repository.UserRepository;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MosqueService {

	private final MosqueRepository mosqueRepository;
	private final UserRepository userRepository;
	private final MosqueMembershipService mosqueMembershipService;

	public MosqueService(
	        MosqueRepository mosqueRepository,
	        UserRepository userRepository,
	        MosqueMembershipService mosqueMembershipService) {

	    this.mosqueRepository = mosqueRepository;
	    this.userRepository = userRepository;
	    this.mosqueMembershipService = mosqueMembershipService;
	}

	@Transactional
	public MosqueResponse createMosque(CreateMosqueRequest request) {

	    Mosque mosque = MosqueMapper.toEntity(request);

	    Mosque savedMosque = mosqueRepository.save(mosque);

	    String username = SecurityUtils.getCurrentUsername();

	    User currentUser = userRepository.findByUsername(username)
	            .orElseThrow();
	    
	    mosqueMembershipService.createFounderMembership(
	            savedMosque,
	            currentUser
	    );

	    return MosqueMapper.toResponse(savedMosque);
	}

    public List<MosqueResponse> getAllMosques() {

        return mosqueRepository.findAll()
                .stream()
                .map(MosqueMapper::toResponse)
                .collect(Collectors.toList());
    }

    public MosqueResponse getMosqueById(UUID id) {

        Mosque mosque = mosqueRepository.findById(id)
        		.orElseThrow(MosqueNotFoundException::new);

        return MosqueMapper.toResponse(mosque);
    }

}