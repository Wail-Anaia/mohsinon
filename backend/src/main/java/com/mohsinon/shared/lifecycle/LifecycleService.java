package com.mohsinon.shared.lifecycle;

import org.springframework.stereotype.Service;

import com.mohsinon.modules.users.entity.User;

@Service
public class LifecycleService {

    /*
     * Active
     */

    public void activate(
            Activatable entity) {

        LifecycleUtils.activate(entity);
    }

    public void deactivate(
            Activatable entity) {

        LifecycleUtils.deactivate(entity);
    }

    /*
     * Archive
     */

    public void archive(
            Archivable entity,
            User user) {

        LifecycleUtils.archive(entity, user);
    }

    public void restore(
            Archivable entity) {

        LifecycleUtils.restore(entity);
    }

    /*
     * Soft Delete
     */

    public void softDelete(
            SoftDeletable entity,
            User user) {

        LifecycleUtils.softDelete(entity, user);
    }

    public void restoreDeleted(
            SoftDeletable entity) {

        LifecycleUtils.restoreDeleted(entity);
    }

}