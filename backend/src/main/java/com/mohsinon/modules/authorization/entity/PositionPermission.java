package com.mohsinon.modules.authorization.entity;

import com.mohsinon.modules.mosques.entity.MosquePosition;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "positions_permissions")
public class PositionPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "position_id")
    private MosquePosition position;

    @ManyToOne(optional = false)
    @JoinColumn(name = "permission_id")
    private Permission permission;

    public PositionPermission() {
    }

}