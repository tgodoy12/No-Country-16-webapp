package com.healthtech.dto;

import com.healthtech.entity.Appointment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentResponse {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private String patientName;
    private String doctorName;
    private LocalDateTime appointmentDateTime;
    private LocalDateTime endDateTime;
    private Appointment.AppointmentType type;
    private Appointment.AppointmentStatus status;
    private String reason;
    private String symptoms;
    private String meetingUrl;
    private String notes;
    private LocalDateTime createdAt;

    public static AppointmentResponse fromEntity(Appointment appointment) {
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setPatientId(appointment.getPatient().getId());
        response.setDoctorId(appointment.getDoctor().getId());
        response.setPatientName(appointment.getPatient().getFullName());
        response.setDoctorName(appointment.getDoctor().getFullName());
        response.setAppointmentDateTime(appointment.getAppointmentDateTime());
        response.setEndDateTime(appointment.getEndDateTime());
        response.setType(appointment.getType());
        response.setStatus(appointment.getStatus());
        response.setReason(appointment.getReason());
        response.setSymptoms(appointment.getSymptoms());
        response.setMeetingUrl(appointment.getMeetingUrl());
        response.setNotes(appointment.getNotes());
        response.setCreatedAt(appointment.getCreatedAt());
        return response;
    }
}