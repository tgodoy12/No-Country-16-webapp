package com.healthtech.repository;

import com.healthtech.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    // Métodos adicionales para buscar por rol
    @Query("SELECT u FROM User u WHERE u.id = :id AND u.role = 'ROLE_PATIENT'")
    Optional<User> findPatientById(@Param("id") Long id);

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.role = 'ROLE_DOCTOR'")
    Optional<User> findDoctorById(@Param("id") Long id);
}