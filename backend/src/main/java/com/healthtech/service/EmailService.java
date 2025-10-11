package com.healthtech.service;

import com.healthtech.entity.Appointment;
import com.healthtech.entity.Doctor;
import com.healthtech.entity.Patient;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendAppointmentConfirmation(Patient patient, Doctor doctor, Appointment appointment) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(patient.getEmail());
            helper.setSubject("✅ Confirmación de Cita Médica - HealthTech");
            helper.setText(buildAppointmentConfirmationEmail(patient, doctor, appointment), true);

            mailSender.send(message);
            System.out.println("✅ Email HTML enviado exitosamente a: " + patient.getEmail());

        } catch (MessagingException e) {
            System.out.println("❌ Error enviando email HTML: " + e.getMessage());
            // Fallback a texto plano
            sendSimpleAppointmentConfirmation(patient, doctor, appointment);
        }
    }

    private String buildAppointmentConfirmationEmail(Patient patient, Doctor doctor, Appointment appointment) {
        String formattedDate = formatAppointmentDate(appointment.getAppointmentDateTime());
        String formattedTime = formatAppointmentTime(appointment.getAppointmentDateTime());
        String meetingInfo = appointment.getType() == Appointment.AppointmentType.VIRTUAL ?
                "<p><strong>🔗 Enlace de videollamada:</strong> <a href='" + appointment.getMeetingUrl() + "'>" + appointment.getMeetingUrl() + "</a></p>" :
                "<p><strong>📍 Consultorio:</strong> " + doctor.getSpecialization() + " - Piso 2</p>";

        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { 
                            font-family: 'Arial', sans-serif; 
                            line-height: 1.6; 
                            color: #333; 
                            max-width: 600px; 
                            margin: 0 auto; 
                            padding: 20px;
                        }
                        .header { 
                            background: #4CAF50;
                            color: white; 
                            padding: 30px; 
                            text-align: center; 
                            border-radius: 10px 10px 0 0;
                        }
                        .content { 
                            background: #f9f9f9; 
                            padding: 30px; 
                            border-radius: 0 0 10px 10px;
                        }
                        .appointment-card {
                            background: white;
                            padding: 20px;
                            border-radius: 8px;
                            border-left: 4px solid #4CAF50;
                            margin: 20px 0;
                            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                        }
                        .footer { 
                            text-align: center; 
                            margin-top: 20px; 
                            color: #666; 
                            font-size: 12px;
                        }
                    </style>
                </head>
                <body>
                    <div class="header">
                        <h1>🏥 HealthTech</h1>
                        <h2>¡Su cita ha sido confirmada!</h2>
                    </div>
                
                    <div class="content">
                        <p>Estimado/a <strong>%s</strong>,</p>
                        <p>Su cita médica ha sido confirmada exitosamente. A continuación los detalles:</p>
                
                        <div class="appointment-card">
                            <h3>📅 Detalles de la Cita</h3>
                            <p><strong>👨‍⚕️ Doctor:</strong> %s</p>
                            <p><strong>🎯 Especialidad:</strong> %s</p>
                            <p><strong>📅 Fecha:</strong> %s</p>
                            <p><strong>⏰ Hora:</strong> %s</p>
                            <p><strong>💻 Modalidad:</strong> %s</p>
                            %s
                            <p><strong>📝 Motivo:</strong> %s</p>
                        </div>
                
                        <p><strong>💡 Recordatorio importante:</strong></p>
                        <ul>
                            <li>Llegue 10 minutos antes de su cita</li>
                            <li>Traiga su identificación</li>
                            %s
                        </ul>
                
                        <p>Si necesita cancelar o reprogramar, puede hacerlo desde su portal de paciente.</p>
                    </div>
                
                    <div class="footer">
                        <p>Saludos cordiales,<br><strong>Equipo HealthTech</strong></p>
                        <p>📞 +1 234 567 8900 | ✉️ info@healthtech.com</p>
                    </div>
                </body>
                </html>
                """.formatted(
                patient.getFirstName() + " " + patient.getLastName(),
                doctor.getFullName(),
                doctor.getSpecialization(),
                formattedDate,
                formattedTime,
                appointment.getType() == Appointment.AppointmentType.VIRTUAL ? "Virtual" : "Presencial",
                meetingInfo,
                appointment.getReason(),
                appointment.getType() == Appointment.AppointmentType.VIRTUAL ?
                        "<li>Verifique su conexión a internet antes de la cita</li>" :
                        "<li>Traer estudios médicos previos si los tiene</li>"
        );
    }

    private String formatAppointmentDate(LocalDateTime dateTime) {
        try {
            String dayName = dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
            String monthName = dateTime.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
            return String.format("%s, %d de %s de %d",
                    capitalize(dayName), dateTime.getDayOfMonth(), monthName, dateTime.getYear());
        } catch (Exception e) {
            // Fallback simple si hay error
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return dateTime.format(formatter);
        }
    }

    private String formatAppointmentTime(LocalDateTime dateTime) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
            return dateTime.format(formatter);
        } catch (Exception e) {
            // Fallback simple si hay error
            return dateTime.getHour() + ":" + String.format("%02d", dateTime.getMinute());
        }
    }

    private String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    // Fallback para texto plano (SIMPLIFICADO)
    private void sendSimpleAppointmentConfirmation(Patient patient, Doctor doctor, Appointment appointment) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(patient.getEmail());
            message.setSubject("Confirmación de Cita Médica - HealthTech");

            // Formato simple y seguro para texto plano
            String fechaSimple = appointment.getAppointmentDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String horaSimple = appointment.getAppointmentDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));

            message.setText(
                    "Estimado/a " + patient.getFirstName() + " " + patient.getLastName() + ",\n\n" +
                            "Su cita médica ha sido confirmada:\n\n" +
                            "Doctor: " + doctor.getFullName() + "\n" +
                            "Especialidad: " + doctor.getSpecialization() + "\n" +
                            "Fecha: " + fechaSimple + "\n" +
                            "Hora: " + horaSimple + "\n" +
                            "Tipo: " + (appointment.getType() == Appointment.AppointmentType.VIRTUAL ? "Virtual" : "Presencial") + "\n" +
                            (appointment.getType() == Appointment.AppointmentType.VIRTUAL ?
                                    "Enlace: " + appointment.getMeetingUrl() + "\n" : "") +
                            "Motivo: " + appointment.getReason() + "\n\n" +
                            "Saludos cordiales,\nEquipo HealthTech"
            );

            mailSender.send(message);
            System.out.println("✅ Email de texto plano enviado exitosamente a: " + patient.getEmail());

        } catch (Exception e) {
            System.out.println("❌ Error crítico enviando email: " + e.getMessage());
        }
    }

    public void sendAppointmentCancellation(Patient patient, Doctor doctor, Appointment appointment) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(patient.getEmail());
            message.setSubject("Cancelación de Cita Médica - HealthTech");

            String fechaSimple = appointment.getAppointmentDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String horaSimple = appointment.getAppointmentDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));

            message.setText(
                    "Estimado/a " + patient.getFirstName() + " " + patient.getLastName() + ",\n\n" +
                            "Su cita médica ha sido cancelada:\n\n" +
                            "Doctor: " + doctor.getFullName() + "\n" +
                            "Fecha: " + fechaSimple + "\n" +
                            "Hora: " + horaSimple + "\n" +
                            "\nPuede agendar una nueva cita a través de nuestro portal.\n\n" +
                            "Saludos cordiales,\nEquipo HealthTech"
            );

            mailSender.send(message);
            System.out.println("✅ Email de cancelación enviado a: " + patient.getEmail());

        } catch (Exception e) {
            System.out.println("❌ Error enviando email de cancelación: " + e.getMessage());
        }
    }
}