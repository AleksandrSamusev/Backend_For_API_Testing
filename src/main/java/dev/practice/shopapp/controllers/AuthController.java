package dev.practice.shopapp.controllers;

import dev.practice.shopapp.dto.JwtResponse;
import dev.practice.shopapp.dto.LoginRequest;
import dev.practice.shopapp.dto.RegisterRequest;
import dev.practice.shopapp.services.AuthService;
import dev.practice.shopapp.models.ApiResponse;
import dev.practice.shopapp.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 🚀 INITIATE ACCESS: The Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> authenticateUser(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest request) {

        JwtResponse jwtResponse = authService.login(loginRequest);

        return ResponseEntity.ok(
                ResponseUtil.success(jwtResponse, "Clearance Granted. Welcome Commander.", request.getRequestURI())
        );
    }

    // 🚀 FORGE CREDENTIALS: The Register Endpoint
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> registerUser(
            @Valid @RequestBody RegisterRequest registerRequest,
            HttpServletRequest request) {

        String message = authService.register(registerRequest);

        return ResponseEntity.ok(
                ResponseUtil.success(message, "New Credentials Forged.", request.getRequestURI())
        );
    }
}
