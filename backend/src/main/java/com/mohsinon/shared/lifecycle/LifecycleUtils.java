package com.mohsinon.shared.lifecycle;

import java.time.LocalDateTime;

import com.mohsinon.modules.users.entity.User;

public final class LifecycleUtils {

    private LifecycleUtils() {
    }

    /*
     * Active
     */

    public static void activate(Activatable entity) {

        entity.setActive(true);
    }

    public static void deactivate(Activatable entity) {

        entity.setActive(false);
    }

    /*
     * Archive
     */

    public static void archive(
            Archivable entity,
            User user) {

        entity.setArchived(true);
        entity.setArchivedAt(LocalDateTime.now());
        entity.setArchivedBy(user);
    }

    public static void restore(
            Archivable entity) {

        entity.setArchived(false);
        entity.setArchivedAt(null);
        entity.setArchivedBy(null);
    }

    /*
     * Soft Delete
     */

    public static void softDelete(
            SoftDeletable entity,
            User user) {

        entity.setDeleted(true);
        entity.setDeletedAt(LocalDateTime.now());
        entity.setDeletedBy(user);
    }

    public static void restoreDeleted(
            SoftDeletable entity) {

        entity.setDeleted(false);
        entity.setDeletedAt(null);
        entity.setDeletedBy(null);
    }

}