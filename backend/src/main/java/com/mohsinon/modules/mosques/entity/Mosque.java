package com.mohsinon.modules.mosques.entity;

import com.mohsinon.shared.entity.LifecycleEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "mosques")
public class Mosque extends LifecycleEntity{

    @Column(nullable = false)
    private String name;

    @Column(length = 3000)
    private String description;

    private String country;

    private String city;

    private String district;

    private String address;

    private Double latitude;

    private Double longitude;

    private String phone;

    private String email;

    @Column(nullable = false)
    private Boolean verified = false;

    public Mosque() {
    }
}