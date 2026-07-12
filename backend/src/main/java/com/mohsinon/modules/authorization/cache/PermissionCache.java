package com.mohsinon.modules.authorization.cache;

import java.util.Set;
import java.util.UUID;

public interface PermissionCache {

    Set<String> get(UUID userId);

    void put(
            UUID userId,
            Set<String> permissions);

    void evict(UUID userId);

    void clear();

}