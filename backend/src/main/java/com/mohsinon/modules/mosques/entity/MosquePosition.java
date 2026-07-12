package com.mohsinon.modules.mosques.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "mosque_positions")
public class MosquePosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Boolean active = true;
    
    @Column(nullable = false)
    private Boolean uniquePosition = false;

	public MosquePosition() {
    }
    
}