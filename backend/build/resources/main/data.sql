-- Eliminar data.sql existente y reemplazar con:

INSERT INTO users (id, email, password, first_name, last_name, phone, role, enabled, created_at, updated_at)
VALUES
(1, 'admin@healthtech.com', '$2a$10$ABCDE12345FGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopq', 'Admin', 'Sistema', '+1234567890', 'ROLE_ADMIN', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'dr.garcia@healthtech.com', '$2a$10$ABCDE12345FGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopq', 'Carlos', 'García', '+1234567891', 'ROLE_DOCTOR', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'dr.lopez@healthtech.com', '$2a$10$ABCDE12345FGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopq', 'María', 'López', '+1234567892', 'ROLE_DOCTOR', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'paciente1@healthtech.com', '$2a$10$ABCDE12345FGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopq', 'Juan', 'Pérez', '+1234567893', 'ROLE_PATIENT', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'paciente2@healthtech.com', '$2a$10$ABCDE12345FGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopq', 'Ana', 'Martínez', '+1234567894', 'ROLE_PATIENT', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO doctors (user_id, license_number, specialization, qualification, experience_years, bio)
VALUES
(2, 'MED12345', 'Cardiología', 'MD, PhD', 15, 'Especialista en cardiología con 15 años de experiencia'),
(3, 'MED67890', 'Pediatría', 'MD, MSc', 10, 'Pediatra especializada en atención infantil');

INSERT INTO patients (user_id, identification_number, date_of_birth, address, emergency_contact, blood_type, allergies)
VALUES
(4, 'ID123456', '1985-05-15', 'Calle Principal 123', '+1234567890', 'A+', 'Penicilina'),
(5, 'ID789012', '1990-08-22', 'Avenida Central 456', '+1234567891', 'O-', 'Ninguna');