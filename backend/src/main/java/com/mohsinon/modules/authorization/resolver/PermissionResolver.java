package com.mohsinon.modules.authorization.resolver;

import java.util.Set;

import com.mohsinon.modules.mosques.entity.MosqueMembership;
import com.mohsinon.modules.users.entity.User;

public interface PermissionResolver {

    Set<String> resolve(
            User user,
            MosqueMembership membership);

}