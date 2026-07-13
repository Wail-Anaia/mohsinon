package com.mohsinon.modules.donations.mapper;

import com.mohsinon.modules.donations.dto.request.CreateDonationCategoryRequest;
import com.mohsinon.modules.donations.dto.request.CreateDonationRequest;
import com.mohsinon.modules.donations.dto.request.UpdateDonationCategoryRequest;
import com.mohsinon.modules.donations.dto.response.DonationCategoryResponse;
import com.mohsinon.modules.donations.dto.response.DonationResponse;
import com.mohsinon.modules.donations.entity.Donation;
import com.mohsinon.modules.donations.entity.DonationCategory;
import com.mohsinon.modules.mosques.entity.Mosque;
import com.mohsinon.modules.users.entity.User;

import org.springframework.stereotype.Component;

@Component
public class DonationMapper {

    public DonationCategory toEntity(CreateDonationCategoryRequest request) {

        DonationCategory category = new DonationCategory();

        category.setCode(request.getCode());
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setActive(request.getActive());
        category.setSortOrder(request.getSortOrder());

        return category;
    }

    public void updateEntity(
            DonationCategory category,
            UpdateDonationCategoryRequest request) {

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setActive(request.getActive());
        category.setSortOrder(request.getSortOrder());
    }

    public DonationCategoryResponse toResponse(DonationCategory category) {

        DonationCategoryResponse response = new DonationCategoryResponse();

        response.setId(category.getId());
        response.setCode(category.getCode());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setActive(category.isActive());
        response.setSortOrder(category.getSortOrder());
        response.setCreatedAt(category.getCreatedAt());
        response.setUpdatedAt(category.getUpdatedAt());

        return response;
    }
    
    public Donation toEntity(
            CreateDonationRequest request,
            Mosque mosque,
            DonationCategory category,
            User donor) {

        Donation donation = new Donation();

        donation.setMosque(mosque);
        donation.setCategory(category);
        donation.setDonor(donor);

        donation.setTitle(request.getTitle());
        donation.setDescription(request.getDescription());

        donation.setType(request.getType());
        donation.setQuantity(request.getQuantity());
        donation.setEstimatedValue(request.getEstimatedValue());
        donation.setCurrency(request.getCurrency());

        return donation;
    }
    
    public DonationResponse toResponse(Donation donation) {

        DonationResponse response = new DonationResponse();

        response.setId(donation.getId());
        response.setMosqueId(donation.getMosque().getId());
        response.setCategoryId(donation.getCategory().getId());

        if (donation.getDonor() != null) {
            response.setDonorId(donation.getDonor().getId());
        }

        response.setTitle(donation.getTitle());
        response.setDescription(donation.getDescription());

        response.setType(donation.getType());
        response.setStatus(donation.getStatus());

        response.setQuantity(donation.getQuantity());
        response.setEstimatedValue(donation.getEstimatedValue());
        response.setCurrency(donation.getCurrency());

        response.setCreatedAt(donation.getCreatedAt());

        return response;
    }
}