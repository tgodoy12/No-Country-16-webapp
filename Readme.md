# 🏥 Portal Web de Coordinación de Citas y Teleasistencia

**Vertical:** Web App  
**Sector de Negocio:** HealthTech  
**Equipo:** 16  

### 👥 Miembros del Equipo
- **Pamela Pilotti** – Project Manager  
- **Cristian Albornoz** – Backend Developer  
- **David Merbello** – Backend Developer  

---

# 🏥 HealthTech - Backend del Sistema de Telemedicina

## 📋 Descripción

Backend completo para un sistema de telemedicina que permite la **gestión de citas médicas**, **videoconsultas** y **comunicación entre pacientes y doctores**.  
Desarrollado con **Spring Boot 3.2** y **Java 17** para garantizar escalabilidad, seguridad y rendimiento.

---

## 🚀 Características Principales

### ✅ Funcionalidades Implementadas

- **👥 Gestión de Usuarios:** Registro y autenticación con JWT  
- **📅 Sistema de Citas Inteligente:** Validación de horarios y disponibilidad  
- **🎥 Videollamadas Integradas:** WebRTC con salas privadas  
- **💬 Chat en Tiempo Real:** Comunicación directa durante la consulta  
- **📧 Notificaciones Automáticas:** Emails con confirmaciones y recordatorios  
- **🛡️ Seguridad Robusta:** Spring Security con roles y permisos  
- **🏥 Historial Médico:** Gestión de registros médicos  

---

## 🏗️ Arquitectura Técnica

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 17 | Lenguaje principal |
| Spring Boot | 3.2.0 | Framework backend |
| Spring Security | 6.2.0 | Autenticación y autorización |
| JWT | 0.11.5 | Tokens de seguridad |
| MySQL | 8.0 | Base de datos principal |
| H2 Database | 2.2.220 | Base de datos desarrollo |
| WebSocket | - | Comunicación tiempo real |
| Gradle | 8.4 | Gestión de dependencias |
| Lombok | - | Reducción de código repetitivo |

**Patrones de Diseño:**  
`MVC`, `DTO`, `Repository`, `Service Layer`, `JWT Authentication`

---

## 📁 Estructura del Proyecto

```
src/main/java/com/healthtech/
├── config/
├── controller/
├── entity/
├── repository/
├── service/
├── security/
└── dto/
```

---

## 🔌 API Documentation

### Autenticación

#### Registrar Usuario
```http
POST /api/auth/register
```
```json
{
  "email": "usuario@healthtech.com",
  "password": "password123",
  "firstName": "Juan",
  "lastName": "Pérez",
  "phone": "+1234567890",
  "role": "PATIENT"
}
```

#### Iniciar Sesión
```http
POST /api/auth/login
```

---

### Gestión de Citas

#### Crear Cita
```http
POST /api/appointments
Authorization: Bearer <jwt-token>
```
```json
{
  "doctorId": 1,
  "patientId": 1,
  "appointmentDateTime": "2024-12-15T10:00:00",
  "type": "VIRTUAL",
  "reason": "Consulta general",
  "symptoms": "Dolor de cabeza"
}
```

#### Obtener Citas del Paciente
```http
GET /api/appointments/patient/1
Authorization: Bearer <jwt-token>
```

---

### Videollamadas
```http
POST /api/video-call/{appointmentId}/start
POST /api/video-call/{meetingId}/end
```

---

### Doctores
```http
GET /api/doctors
GET /api/doctors/specialization/{especialidad}
```

---

## 🛠️ Instalación y Configuración

### Prerrequisitos
- Java 17+
- MySQL 8+
- Gradle 8.4+

### Pasos

```bash
git clone <repository-url>
cd teleasistencia-mvp
```

Crear base de datos:
```sql
CREATE DATABASE teleasistencia;
```

Configurar `application-dev.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/teleasistencia
    username: your_username
    password: your_password
```

Ejecutar:
```bash
./gradlew bootRun
```

Verificar:
```bash
curl http://localhost:8080/api/auth/health
```

---

## 🧪 Testing

1. Registrar usuario  
2. Iniciar sesión y guardar token JWT  
3. Crear cita  
4. Probar videollamada  

Colección Postman disponible en `/postman/`

---

## 🔒 Seguridad

- Autenticación JWT  
- Roles: `ROLE_PATIENT`, `ROLE_DOCTOR`, `ROLE_ADMIN`  
- BCrypt para contraseñas  
- CORS configurado  
- Validación de entrada  

---

## 🚀 Despliegue

### Desarrollo
```bash
./gradlew bootRun
```

### Producción
```bash
./gradlew clean build
java -jar build/libs/teleasistencia-mvp.jar
```

---

## 📊 Estado del Proyecto

| Estado | Funcionalidad |
|--------|----------------|
| ✅ | Autenticación y usuarios |
| ✅ | Sistema de citas |
| ✅ | Videollamadas WebRTC |
| ✅ | Chat en tiempo real |
| ✅ | Emails automáticos |
| 🔄 | Frontend React |
| 🔄 | Integración EHR |
| 🔄 | Panel administrativo |

---

## 🤝 Contribución

1. Fork del proyecto  
2. Crear rama `feature/...`  
3. Commit y push  
4. Pull Request  

---

## 📝 Licencia

Este proyecto está bajo la **Licencia MIT**.  
Consulta el archivo `LICENSE` para más detalles.

---

## 📞 Soporte

1. Revisar documentación de APIs  
2. Ver logs de aplicación  
3. Usar ejemplos en Postman  
4. Crear un issue en el repositorio  

---

## 💡 Equipo HealthTech

Desarrollado con ❤️ por el equipo HealthTech para revolucionar la atención médica digital.
