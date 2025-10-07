-- Creamos las tablas al iniciar nuetra aplicacion

CREATE TABLE rol (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    rol_id BIGINT,
    FOREIGN KEY (rol_id) REFERENCES rol(id)
);

CREATE TABLE paciente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    documento VARCHAR(20) UNIQUE,
    telefono VARCHAR(20),
    direccion VARCHAR(150),
    fecha_nacimiento DATE,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE medico (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    especialidad VARCHAR(100),
    licencia VARCHAR(50) UNIQUE,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);
