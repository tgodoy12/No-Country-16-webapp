package com.healthtech.service;

import com.healthtech.dto.LoginRequest;
import com.healthtech.dto.LoginResponse;
import com.healthtech.dto.RegisterRequest;
import com.healthtech.dto.RegisterResponse;
import com.healthtech.entity.Doctor;
import com.healthtech.entity.Patient;
import com.healthtech.entity.User;
import com.healthtech.repository.UserRepository;
import com.healthtech.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return new LoginResponse(jwt, user.getId(), user.getEmail(),
                user.getFirstName(), user.getLastName(), user.getRole().name());
    }

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        User user;

        switch (request.getRole().toUpperCase()) {
            case "DOCTOR":
                Doctor doctor = new Doctor();
                doctor.setLicenseNumber("LIC" + System.currentTimeMillis());
                user = doctor;
                break;
            case "ADMIN":
                user = new User();
                break;
            default:
                user = new Patient();
        }

        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setRole(User.UserRole.valueOf("ROLE_" + request.getRole().toUpperCase()));

        User savedUser = userRepository.save(user);
        return RegisterResponse.fromUser(savedUser);
    }
}