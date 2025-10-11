package com.healthtech.controller;

import com.healthtech.dto.AppointmentRequest;
import com.healthtech.dto.AppointmentResponse;
import com.healthtech.entity.Appointment;
import com.healthtech.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(@Valid @RequestBody AppointmentRequest request) {
        // Usar el patientId del request en lugar del fijo
        AppointmentResponse appointment = appointmentService.createAppointment(
                request.getPatientId(),  // ← CAMBIAR ESTA LÍNEA
                request.getDoctorId(),
                request.getAppointmentDateTime(),
                request.getType(),
                request.getReason(),
                request.getSymptoms()
        );
        return ResponseEntity.ok(appointment);
    }

    // ... (el resto del código permanece igual)
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponse>> getPatientAppointments(@PathVariable Long patientId) {
        List<AppointmentResponse> appointments = appointmentService.getPatientAppointments(patientId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentResponse>> getDoctorAppointments(@PathVariable Long doctorId) {
        List<AppointmentResponse> appointments = appointmentService.getDoctorAppointments(doctorId);
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/{appointmentId}/status")
    public ResponseEntity<AppointmentResponse> updateAppointmentStatus(
            @PathVariable Long appointmentId,
            @RequestParam Appointment.AppointmentStatus status) {
        AppointmentResponse appointment = appointmentService.updateAppointmentStatus(appointmentId, status);
        return ResponseEntity.ok(appointment);
    }

    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.ok().build();
    }
}