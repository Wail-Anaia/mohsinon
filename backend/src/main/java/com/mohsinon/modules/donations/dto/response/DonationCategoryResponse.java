package com.mohsinon.modules.donations.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DonationCategoryResponse {

    private UUID id;

    private String code;

    private String name;

    private String description;

    private boolean active;

    private Integer sortOrder;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}