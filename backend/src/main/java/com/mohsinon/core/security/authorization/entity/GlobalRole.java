package com.mohsinon.core.security.authorization.entity;

import com.mohsinon.core.domain.BaseEntity;
import com.mohsinon.core.security.authorization.model.GlobalRoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.UUID;

/**
 * Persistent entity representing a global system role.
 */
@Entity
@Table(
        name = "global_roles",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_global_roles_name", columnNames = "name")
        },
        indexes = {
                @Index(name = "idx_global_roles_name", columnList = "name")
        }
)
public class GlobalRole extends BaseEntity {

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    public GlobalRole() {
    }

    public GlobalRole(UUID id, String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
    }

    public GlobalRole(GlobalRoleType roleType, String description) {
        this.name = roleType.name();
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GlobalRoleType toRoleType() {
        try {
            return GlobalRoleType.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
