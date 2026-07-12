package com.mohsinon.modules.users.api;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.mohsinon.modules.users.entity.User;
import com.mohsinon.modules.users.exception.UserNotFoundException;
import com.mohsinon.modules.users.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserApiImpl implements UserApi {

    private final UserRepository repository;

    @Override
    public User getUserById(UUID userId) {

        return repository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(userId));
    }

}