package com.healthtech.repository;

import com.healthtech.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Buscar citas por paciente
    List<Appointment> findByPatientIdOrderByAppointmentDateTimeDesc(Long patientId);

    // Buscar citas por doctor
    List<Appointment> findByDoctorIdOrderByAppointmentDateTimeDesc(Long doctorId);

    // Buscar citas de un doctor en un rango de tiempo (para validar disponibilidad)
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDateTime BETWEEN :start AND :end")
    List<Appointment> findByDoctorIdAndAppointmentDateTimeBetween(
            @Param("doctorId") Long doctorId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    // Buscar citas próximas del paciente
    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND a.status = 'SCHEDULED' AND a.appointmentDateTime > :now")
    List<Appointment> findUpcomingAppointmentsByPatientId(@Param("patientId") Long patientId, @Param("now") LocalDateTime now);

    // Buscar citas próximas del doctor
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.status = 'SCHEDULED' AND a.appointmentDateTime > :now")
    List<Appointment> findUpcomingAppointmentsByDoctorId(@Param("doctorId") Long doctorId, @Param("now") LocalDateTime now);
}