package com.healthtech.controller;

import com.healthtech.entity.Appointment;
import com.healthtech.entity.Doctor;
import com.healthtech.entity.Patient;
import com.healthtech.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestEmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/email")
    public ResponseEntity<Map<String, String>> testEmail(@RequestParam String toEmail) {
        try {
            // Crear datos de prueba
            Patient patient = new Patient();
            patient.setEmail(toEmail);
            patient.setFirstName("Juan");
            patient.setLastName("Pérez");

            Doctor doctor = new Doctor();
            doctor.setFirstName("Carlos");
            doctor.setLastName("García");
            doctor.setSpecialization("Cardiología");

            Appointment appointment = new Appointment();
            appointment.setAppointmentDateTime(LocalDateTime.now().plusDays(1));
            appointment.setType(Appointment.AppointmentType.VIRTUAL);
            appointment.setMeetingUrl("https://meet.healthtech.com/test-meeting");

            // Enviar email de prueba
            emailService.sendAppointmentConfirmation(patient, doctor, appointment);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Email de prueba enviado a: " + toEmail);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error enviando email: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}