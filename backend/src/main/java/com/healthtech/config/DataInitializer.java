package com.healthtech.config;

import com.healthtech.entity.User;
import com.healthtech.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verificar si ya existen usuarios
        if (userRepository.count() == 0) {
            // Crear usuario admin por defecto
            User admin = new User();
            admin.setEmail("admin@healthtech.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFirstName("Admin");
            admin.setLastName("Sistema");
            admin.setPhone("+1234567890");
            admin.setRole(User.UserRole.ROLE_ADMIN);

            userRepository.save(admin);
            System.out.println("=== DATOS INICIALES CREADOS ===");
            System.out.println("Usuario admin: admin@healthtech.com / admin123");
        }
    }
}
