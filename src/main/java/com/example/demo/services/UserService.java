package com.example.demo.services;

import com.example.demo.DTO.UserDTO;
import com.example.demo.exceptions.UserAlreadyExists;
import com.example.demo.models.Doctor;
import com.example.demo.models.Patient;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registrar(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.email())) {
            throw new UserAlreadyExists("Ya existe un usuario con el email: " + userDTO.email());
        }

        User newUser;

        if (userDTO.role() == Role.ROLE_PATIENT) {

            newUser = new Patient(userDTO);
        } else if (userDTO.role() == Role.ROLE_DOCTOR) {

            newUser = new Doctor(userDTO);
        } else {

            throw new IllegalArgumentException("Rol de usuario no válido para registro: " + userDTO.role());
        }

        newUser.setPassword(passwordEncoder.encode(userDTO.password()));
        userRepository.save(newUser);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuario no encontrado con email: " + email));
    }
}
