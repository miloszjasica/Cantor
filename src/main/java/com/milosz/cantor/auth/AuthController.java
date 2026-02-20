package com.milosz.cantor.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.milosz.cantor.web.api.dto.AuthResponse;
import com.milosz.cantor.web.api.dto.LoginRequest;
import com.milosz.cantor.web.api.dto.LoginResponse;
import com.milosz.cantor.web.api.dto.RefreshRequest;
import com.milosz.cantor.web.api.dto.RefreshResponse;
import com.milosz.cantor.web.api.dto.RegisterRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.status(201).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(@io.swagger.v3.oas.annotations.parameters.RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }
}