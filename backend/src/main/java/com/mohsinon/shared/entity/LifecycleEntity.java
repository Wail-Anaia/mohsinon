package com.mohsinon.shared.entity;

import com.mohsinon.modules.users.entity.User;
import com.mohsinon.shared.lifecycle.Activatable;
import com.mohsinon.shared.lifecycle.Archivable;
import com.mohsinon.shared.lifecycle.SoftDeletable;

import java.time.LocalDateTime;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class LifecycleEntity extends AuditableEntity
        implements Activatable,
                   Archivable,
                   SoftDeletable {

    private Boolean active = true;

    private Boolean archived = false;

    private LocalDateTime archivedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "archived_by")
    private User archivedBy;

    private Boolean deleted = false;

    private LocalDateTime deletedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by")
    private User deletedBy;
    
    public boolean isActive() {
        return Boolean.TRUE.equals(active);
    }

}