package com.mohsinon.modules.mosques.service;

import com.mohsinon.modules.mosques.dto.request.CreateMosqueRequest;
import com.mohsinon.modules.mosques.dto.response.MosqueResponse;
import com.mohsinon.modules.mosques.entity.Mosque;
import com.mohsinon.modules.mosques.mapper.MosqueMapper;
import com.mohsinon.modules.mosques.repository.MosqueRepository;
import com.mohsinon.modules.mosques.exception.MosqueNotFoundException;
import com.mohsinon.shared.query.response.PageResponse;
import com.mohsinon.shared.query.utils.PaginationUtils;
import com.mohsinon.shared.query.request.SearchRequest;
import com.mohsinon.shared.query.specification.SearchSpecificationFactory;
import com.mohsinon.shared.lifecycle.LifecycleService;
import com.mohsinon.security.utils.SecurityUtils;
import com.mohsinon.modules.users.entity.User;
import com.mohsinon.modules.users.repository.UserRepository;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;
import java.util.UUID;

@Service
public class MosqueService {

	private final MosqueRepository mosqueRepository;
	private final UserRepository userRepository;
	private final MosqueMembershipService mosqueMembershipService;
	private final LifecycleService lifecycleService;

	public MosqueService(
	        MosqueRepository mosqueRepository,
	        UserRepository userRepository,
	        MosqueMembershipService mosqueMembershipService,
	        LifecycleService lifecycleService) {

	    this.mosqueRepository = mosqueRepository;
	    this.userRepository = userRepository;
	    this.mosqueMembershipService = mosqueMembershipService;
	    this.lifecycleService = lifecycleService;
	}

	@Transactional
	public MosqueResponse create(CreateMosqueRequest request) {

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

	public PageResponse<MosqueResponse> search(

	        SearchRequest request,

	        Map<String,String> filters) {

	    Pageable pageable =
	            PaginationUtils.pageable(request);

	    Specification<Mosque> specification =
	            SearchSpecificationFactory.build(filters);

	    Page<Mosque> page =
	            mosqueRepository.findAll(
	                    specification,
	                    pageable);

	    return PaginationUtils.response(
	            page.map(MosqueMapper::toResponse));
	}

	public MosqueResponse findById(UUID id) {

	    Mosque mosque = mosqueRepository.findById(id)
	            .orElseThrow(() -> new MosqueNotFoundException(id));

	    return MosqueMapper.toResponse(mosque);
	}
	
	@Transactional
	public MosqueResponse update(
	        UUID id,
	        CreateMosqueRequest request) {

	    Mosque mosque = mosqueRepository.findById(id)
	            .orElseThrow(() -> new MosqueNotFoundException(id));

	    mosque.setName(request.getName());
	    mosque.setDescription(request.getDescription());
	    mosque.setCountry(request.getCountry());
	    mosque.setCity(request.getCity());
	    mosque.setDistrict(request.getDistrict());
	    mosque.setAddress(request.getAddress());
	    mosque.setLatitude(request.getLatitude());
	    mosque.setLongitude(request.getLongitude());
	    mosque.setPhone(request.getPhone());
	    mosque.setEmail(request.getEmail());

	    Mosque updatedMosque = mosqueRepository.save(mosque);

	    return MosqueMapper.toResponse(updatedMosque);
	}
	
	@Transactional
	public void delete(UUID id) {
		
		String username = SecurityUtils.getCurrentUsername();

		User currentUser = userRepository
		        .findByUsername(username)
		        .orElseThrow();

	    Mosque mosque = mosqueRepository.findById(id)
	            .orElseThrow(() -> new MosqueNotFoundException(id));

	    lifecycleService.softDelete(mosque, currentUser);

	    mosqueRepository.save(mosque);
	}

	@Transactional
	public MosqueResponse restoreDeleted(UUID id) {

	    Mosque mosque = mosqueRepository.findById(id)
	            .orElseThrow(() -> new MosqueNotFoundException(id));

	    lifecycleService.restoreDeleted(mosque);

	    mosqueRepository.save(mosque);

	    return MosqueMapper.toResponse(mosque);
	}
	
	@Transactional
	public MosqueResponse archive(UUID id) {

	    Mosque mosque = mosqueRepository.findById(id)
	            .orElseThrow(() -> new MosqueNotFoundException(id));

	    String username = SecurityUtils.getCurrentUsername();

	    User currentUser = userRepository.findByUsername(username)
	            .orElseThrow();

	    lifecycleService.archive(mosque, currentUser);

	    mosqueRepository.save(mosque);

	    return MosqueMapper.toResponse(mosque);
	}
	
	@Transactional
	public MosqueResponse restoreArchive(UUID id) {

	    Mosque mosque = mosqueRepository.findById(id)
	            .orElseThrow(() -> new MosqueNotFoundException(id));

	    lifecycleService.restore(mosque);

	    mosqueRepository.save(mosque);

	    return MosqueMapper.toResponse(mosque);
	}
	
	@Transactional
	public MosqueResponse activate(UUID id) {

	    Mosque mosque = mosqueRepository.findById(id)
	            .orElseThrow(() -> new MosqueNotFoundException(id));

	    lifecycleService.activate(mosque);

	    mosqueRepository.save(mosque);

	    return MosqueMapper.toResponse(mosque);
	}
	
	@Transactional
	public MosqueResponse deactivate(UUID id) {

	    Mosque mosque = mosqueRepository.findById(id)
	            .orElseThrow(() -> new MosqueNotFoundException(id));

	    lifecycleService.deactivate(mosque);

	    mosqueRepository.save(mosque);

	    return MosqueMapper.toResponse(mosque);
	}
}