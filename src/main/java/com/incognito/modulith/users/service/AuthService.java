package com.incognito.modulith.users.service;

import com.incognito.modulith.users.domain.UserEntity;
import com.incognito.modulith.users.dto.AuthRequest;
import com.incognito.modulith.users.dto.AuthResponse;
import com.incognito.modulith.users.exceptions.UserNotFoundException;
import com.incognito.modulith.users.repository.UserRepository;
import com.incognito.modulith.api.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse authenticate(AuthRequest request) {
        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + request.getUsername()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles());
        claims.put("userType", user.getUserType().name());
        claims.put("email", user.getEmail());

        String token = jwtUtil.generateToken(user.getUsername(), claims);

        log.info("User authenticated successfully: {}", user.getUsername());

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .userType(user.getUserType().name())
                .build();
    }
}