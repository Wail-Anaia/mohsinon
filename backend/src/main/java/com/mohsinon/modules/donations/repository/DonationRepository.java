package com.mohsinon.modules.donations.repository;

import com.mohsinon.modules.donations.entity.Donation;
import com.mohsinon.modules.donations.entity.DonationCategory;
import com.mohsinon.modules.donations.model.DonationStatus;
import com.mohsinon.modules.mosques.entity.Mosque;
import com.mohsinon.modules.users.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DonationRepository extends JpaRepository<Donation, UUID> {

    List<Donation> findByMosque(Mosque mosque);

    List<Donation> findByCategory(DonationCategory category);

    List<Donation> findByStatus(DonationStatus status);

    List<Donation> findByDonor(User donor);

}