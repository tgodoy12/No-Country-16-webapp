package com.healthtech.dto;

import com.healthtech.entity.Appointment;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentRequest {
    @NotNull(message = "ID del doctor es requerido")
    private Long doctorId;

    @NotNull(message = "ID del paciente es requerido")  // ← AGREGAR ESTA LÍNEA
    private Long patientId;

    @NotNull(message = "Fecha y hora de la cita es requerida")
    @Future(message = "La cita debe ser en el futuro")
    private LocalDateTime appointmentDateTime;

    @NotNull(message = "Tipo de cita es requerido")
    private Appointment.AppointmentType type;

    private String reason;

    private String symptoms;
}