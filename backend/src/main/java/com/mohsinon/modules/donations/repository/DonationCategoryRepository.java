package com.mohsinon.modules.donations.repository;

import com.mohsinon.modules.donations.entity.DonationCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DonationCategoryRepository extends JpaRepository<DonationCategory, UUID> {

    Optional<DonationCategory> findByCode(String code);
    
    boolean existsByCode(String code);

}