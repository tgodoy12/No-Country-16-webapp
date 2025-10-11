package com.healthtech.controller;

import com.healthtech.dto.LoginRequest;
import com.healthtech.dto.LoginResponse;
import com.healthtech.dto.RegisterRequest;
import com.healthtech.dto.RegisterResponse;
import com.healthtech.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse user = authService.register(request);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "HealthTech Auth API");
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }
}