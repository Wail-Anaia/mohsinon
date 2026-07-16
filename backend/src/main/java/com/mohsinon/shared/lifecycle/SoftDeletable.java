package com.mohsinon.shared.lifecycle;

import java.time.LocalDateTime;

import com.mohsinon.modules.users.entity.User;

public interface SoftDeletable {

    Boolean getDeleted();

    void setDeleted(Boolean deleted);

    LocalDateTime getDeletedAt();

    void setDeletedAt(LocalDateTime deletedAt);

    User getDeletedBy();

    void setDeletedBy(User user);

}