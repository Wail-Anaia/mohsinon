package com.mohsinon.modules.authorization.entity;

import com.mohsinon.modules.mosques.entity.MosquePosition;
import jakarta.persistence.*;

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

    public Long getId() {
        return id;
    }

    public MosquePosition getPosition() {
        return position;
    }

    public void setPosition(MosquePosition position) {
        this.position = position;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}