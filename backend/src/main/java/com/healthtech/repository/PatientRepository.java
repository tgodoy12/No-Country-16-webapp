package com.healthtech.repository;

import com.healthtech.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByIdentificationNumber(String identificationNumber);

    // Usar @Query para especificar la consulta correctamente
    @Query("SELECT p FROM Patient p WHERE p.id = :userId")
    Optional<Patient> findByUserId(@Param("userId") Long userId);
}