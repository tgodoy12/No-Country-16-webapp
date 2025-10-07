-- Para gestión de citas, agenda y recordatorios.

CREATE TABLE cita (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME NOT NULL,
    tipo ENUM('PRESENCIAL', 'VIRTUAL') NOT NULL,
    motivo TEXT,
    estado ENUM('PENDIENTE', 'CONFIRMADA', 'CANCELADA', 'COMPLETADA') DEFAULT 'PENDIENTE',
    paciente_id BIGINT NOT NULL,
    medico_id BIGINT NOT NULL,
    FOREIGN KEY (paciente_id) REFERENCES paciente(id),
    FOREIGN KEY (medico_id) REFERENCES medico(id)
);

CREATE TABLE recordatorio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cita_id BIGINT NOT NULL,
    fecha_envio DATETIME NOT NULL,
    medio ENUM('EMAIL', 'SMS') DEFAULT 'EMAIL',
    estado ENUM('PENDIENTE', 'ENVIADO', 'FALLIDO') DEFAULT 'PENDIENTE',
    FOREIGN KEY (cita_id) REFERENCES cita(id)
);
