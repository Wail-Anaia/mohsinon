package com.mohsinon.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mohsinon.auth.dto.LoginRequest;
import com.mohsinon.auth.dto.LoginResponse;
import com.mohsinon.auth.dto.RegisterRequest;
import com.mohsinon.auth.dto.RegisterResponse;
import com.mohsinon.common.exception.AuthenticationException;
import com.mohsinon.modules.users.entity.Role;
import com.mohsinon.modules.users.entity.User;
import com.mohsinon.modules.users.exception.RoleNotFoundException;
import com.mohsinon.modules.users.exception.UserAlreadyExistsException;
import com.mohsinon.modules.users.repository.RoleRepository;
import com.mohsinon.modules.users.repository.UserRepository;
import com.mohsinon.security.jwt.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("email", request.getEmail());
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("username", request.getUsername());
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RoleNotFoundException("USER"));

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.getRoles().add(userRole);

        userRepository.save(user);

        return new RegisterResponse("User registered successfully.");
    }

    public LoginResponse login(LoginRequest request) {

    	User user = userRepository
    		    .findByUsernameOrEmail(
    		        request.getUsernameOrEmail(),
    		        request.getUsernameOrEmail()
    		    )
    		    .orElseThrow(() ->
    		        new AuthenticationException("إسم المستخدم أو البريد الإلكتروني  غير صحيح"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new AuthenticationException("كلمة المرور غير صحيحة");
        }

        String token = jwtService.generateToken(user.getUsername());

        return new LoginResponse(token);
    }

}