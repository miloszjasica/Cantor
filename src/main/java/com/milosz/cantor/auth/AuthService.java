package com.milosz.cantor.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.milosz.cantor.security.JwtService;
import com.milosz.cantor.user.User;
import com.milosz.cantor.user.UserRepository;
import com.milosz.cantor.web.api.dto.AuthResponse;
import com.milosz.cantor.web.api.dto.LoginRequest;
import com.milosz.cantor.web.api.dto.LoginResponse;
import com.milosz.cantor.web.api.dto.RefreshRequest;
import com.milosz.cantor.web.api.dto.RefreshResponse;
import com.milosz.cantor.web.api.dto.RegisterRequest;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUsername(request.getUsername());

        userRepository.save(user);

        return new AuthResponse(user.getId().toString(), user.getEmail());
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtService.generateToken(user.getEmail());

        return new LoginResponse(
                accessToken,
                null,
                "Bearer",
                900
        );
    }

    public RefreshResponse refreshToken(RefreshRequest request) {
        String email = jwtService.extractEmail(request.getRefreshToken());
        String newAccessToken = jwtService.generateToken(email);

        return new RefreshResponse(newAccessToken, newAccessToken, 900);
    }
}