package com.mohsinon.auth.service;

import com.mohsinon.auth.dto.RegisterRequest;
import com.mohsinon.auth.dto.RegisterResponse;
import com.mohsinon.auth.dto.LoginRequest;
import com.mohsinon.auth.dto.LoginResponse;
import com.mohsinon.exception.AuthenticationException;
import com.mohsinon.exception.ResourceAlreadyExistsException;
import com.mohsinon.exception.ResourceNotFoundException;
import com.mohsinon.role.entity.Role;
import com.mohsinon.role.repository.RoleRepository;
import com.mohsinon.users.entity.User;
import com.mohsinon.users.repository.UserRepository;
import com.mohsinon.security.jwt.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
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
        	throw new ResourceAlreadyExistsException("Email already exists");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
        	throw new ResourceAlreadyExistsException("Username already exists");
        }

        Role userRole = roleRepository.findByName("USER")
        		.orElseThrow(() -> new ResourceNotFoundException("Role USER not found"));

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

        User user = userRepository.findByEmail(request.getEmail())
        		.orElseThrow(() -> new AuthenticationException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        	throw new AuthenticationException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getUsername());

        return new LoginResponse(token);
    }

}