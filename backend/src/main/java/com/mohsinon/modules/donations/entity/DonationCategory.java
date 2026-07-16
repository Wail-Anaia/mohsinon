package com.mohsinon.modules.donations.entity;

import com.mohsinon.shared.entity.LifecycleEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "donation_categories")
public class DonationCategory extends LifecycleEntity{

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Integer sortOrder = 0;
}