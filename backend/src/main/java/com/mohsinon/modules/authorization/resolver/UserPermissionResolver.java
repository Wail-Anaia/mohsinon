package com.mohsinon.modules.authorization.resolver;

import java.util.Set;
import java.util.UUID;

public interface UserPermissionResolver {

    Set<String> resolve(UUID userId);

}