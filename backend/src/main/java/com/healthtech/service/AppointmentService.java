package com.healthtech.service;

import com.healthtech.dto.AppointmentResponse;
import com.healthtech.entity.Appointment;
import com.healthtech.entity.Doctor;
import com.healthtech.entity.DoctorSchedule;
import com.healthtech.entity.Patient;
import com.healthtech.repository.AppointmentRepository;
import com.healthtech.repository.DoctorRepository;
import com.healthtech.repository.DoctorScheduleRepository;
import com.healthtech.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorScheduleRepository doctorScheduleRepository; // NUEVO

    @Autowired
    private EmailService emailService;

    @Value("${app.video-call.base-url:https://meet.healthtech.com}")
    private String videoCallBaseUrl;

    public AppointmentResponse createAppointment(Long patientId, Long doctorId, LocalDateTime dateTime,
                                                 Appointment.AppointmentType type, String reason, String symptoms) {
        // 1. Validar que paciente y doctor existen
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));

        // 2. Validar disponibilidad COMPLETA del doctor
        if (!isDoctorAvailable(doctorId, dateTime)) {
            throw new RuntimeException("El doctor no está disponible en este horario");
        }

        // 3. Crear la cita
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDateTime(dateTime);
        appointment.setEndDateTime(dateTime.plusHours(1)); // Citas de 1 hora
        appointment.setType(type);
        appointment.setReason(reason);
        appointment.setSymptoms(symptoms);

        // 4. Si es virtual, generar enlace de meeting
        if (type == Appointment.AppointmentType.VIRTUAL) {
            appointment.setMeetingUrl(generateMeetingUrl());
        }

        // 5. Guardar la cita
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // 6. Enviar email de confirmación
        emailService.sendAppointmentConfirmation(patient, doctor, savedAppointment);

        // 7. Devolver respuesta
        return AppointmentResponse.fromEntity(savedAppointment);
    }

    private boolean isDoctorAvailable(Long doctorId, LocalDateTime dateTime) {
        // 1. Validar que no haya citas superpuestas
        boolean hasConflictingAppointments = !appointmentRepository
                .findByDoctorIdAndAppointmentDateTimeBetween(doctorId,
                        dateTime.minusMinutes(59), dateTime.plusMinutes(59))
                .isEmpty();

        if (hasConflictingAppointments) {
            System.out.println("❌ Doctor no disponible: Ya tiene una cita en ese horario");
            return false;
        }

        // 2. Validar horario laboral del doctor
        boolean isWithinWorkingHours = isWithinDoctorWorkingHours(doctorId, dateTime);
        if (!isWithinWorkingHours) {
            System.out.println("❌ Doctor no disponible: Fuera de horario laboral");
            return false;
        }

        System.out.println("✅ Doctor disponible en: " + dateTime);
        return true;
    }

    private boolean isWithinDoctorWorkingHours(Long doctorId, LocalDateTime dateTime) {
        try {
            DayOfWeek appointmentDay = dateTime.getDayOfWeek();
            LocalTime appointmentTime = dateTime.toLocalTime();

            // Buscar horarios del doctor para ese día
            List<DoctorSchedule> schedules = doctorScheduleRepository
                    .findByDoctorIdAndDayOfWeek(doctorId, appointmentDay);

            if (schedules.isEmpty()) {
                System.out.println("⚠️ Doctor no tiene horario configurado para: " + appointmentDay);
                // Si no tiene horario configurado, permitimos la cita (flexibilidad)
                return true;
            }

            // Verificar si la hora de la cita está dentro de algún horario laboral
            for (DoctorSchedule schedule : schedules) {
                if (appointmentTime.isAfter(schedule.getStartTime().minusMinutes(1)) &&
                        appointmentTime.isBefore(schedule.getEndTime())) {
                    System.out.println("✅ Cita dentro del horario: " + schedule.getStartTime() + " - " + schedule.getEndTime());
                    return true;
                }
            }

            System.out.println("❌ Cita fuera de horarios configurados");
            return false;

        } catch (Exception e) {
            System.out.println("⚠️ Error validando horario, permitiendo cita por defecto: " + e.getMessage());
            return true; // Por seguridad, permitir si hay error
        }
    }

    private String generateMeetingUrl() {
        return videoCallBaseUrl + "/" + UUID.randomUUID();
    }

    // ... (el resto de los métodos permanece igual)
    public List<AppointmentResponse> getPatientAppointments(Long patientId) {
        return appointmentRepository.findByPatientIdOrderByAppointmentDateTimeDesc(patientId)
                .stream()
                .map(AppointmentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponse> getDoctorAppointments(Long doctorId) {
        return appointmentRepository.findByDoctorIdOrderByAppointmentDateTimeDesc(doctorId)
                .stream()
                .map(AppointmentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public AppointmentResponse updateAppointmentStatus(Long appointmentId, Appointment.AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        appointment.setStatus(status);
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return AppointmentResponse.fromEntity(updatedAppointment);
    }

    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);

        // Enviar email de cancelación
        emailService.sendAppointmentCancellation(
                appointment.getPatient(),
                appointment.getDoctor(),
                appointment
        );
    }
}