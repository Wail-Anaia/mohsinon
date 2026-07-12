package com.mohsinon.modules.users.api;

import java.util.UUID;

import com.mohsinon.modules.users.entity.User;

public interface UserApi {

    User getUserById(UUID userId);

}