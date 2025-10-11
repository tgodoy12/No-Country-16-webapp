package com.healthtech.repository;

import com.healthtech.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    // Usar @Query para especificar la consulta correctamente
    @Query("SELECT d FROM Doctor d WHERE d.id = :userId")
    Optional<Doctor> findByUserId(@Param("userId") Long userId);

    List<Doctor> findBySpecialization(String specialization);
}