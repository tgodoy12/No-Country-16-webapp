-- Integración Básica con EHR (FHIR), médico quiera ver historial del paciente

CREATE TABLE historial_medico (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    codigo_fhir VARCHAR(50),
    descripcion TEXT,
    fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (paciente_id) REFERENCES paciente(id)
);
