package com.mohsinon.shared.lifecycle;

import java.time.LocalDateTime;

import com.mohsinon.modules.users.entity.User;

public interface Archivable {

    Boolean getArchived();

    void setArchived(Boolean archived);

    LocalDateTime getArchivedAt();

    void setArchivedAt(LocalDateTime archivedAt);

    User getArchivedBy();

    void setArchivedBy(User user);

}