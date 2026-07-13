package com.mohsinon.modules.donations.mapper;

import com.mohsinon.modules.donations.dto.request.CreateDonationCategoryRequest;
import com.mohsinon.modules.donations.dto.request.UpdateDonationCategoryRequest;
import com.mohsinon.modules.donations.dto.response.DonationCategoryResponse;
import com.mohsinon.modules.donations.entity.DonationCategory;
import org.springframework.stereotype.Component;

@Component
public class DonationCategoryMapper {

    /**
     * Convert Create Request -> Entity
     */
    public DonationCategory toEntity(CreateDonationCategoryRequest request) {

        DonationCategory category = new DonationCategory();

        category.setCode(request.getCode());
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setActive(request.getActive());
        category.setSortOrder(request.getSortOrder());

        return category;
    }

    /**
     * Update existing entity from Update Request
     */
    public void updateEntity(
            DonationCategory category,
            UpdateDonationCategoryRequest request) {

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setActive(request.getActive());
        category.setSortOrder(request.getSortOrder());
    }

    /**
     * Convert Entity -> Response DTO
     */
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

}