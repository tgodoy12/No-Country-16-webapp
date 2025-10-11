# 🏥 HealthTech - Sistema de Telemedicina

Sistema completo de gestión de citas médicas y teleasistencia con videollamadas integradas.

---

## 🚀 Características

### ✅ Funcionalidades Implementadas
- 👥 **Gestión de Usuarios**: Registro y autenticación segura con JWT  
- 📅 **Sistema de Citas**: Agendamiento inteligente con validación de horarios  
- 🎥 **Videollamadas**: WebRTC integrado con salas privadas  
- 💬 **Chat en Tiempo Real**: Comunicación durante consultas  
- 📧 **Notificaciones**: Emails automáticos con diseño profesional  
- 🛡️ **Seguridad**: Spring Security con roles y permisos  

---

## 🏗️ Arquitectura
- **Backend**: Spring Boot 3.2 + Java 17  
- **Seguridad**: JWT + Spring Security  
- **Base de Datos**: MySQL + JPA/Hibernate  
- **WebSocket**: Comunicación en tiempo real  
- **Email**: Spring Mail con templates HTML  

---

## 📚 Documentación de APIs

### 🔐 Autenticación
```http
POST /api/auth/register
Content-Type: application/json

{
    "email": "usuario@healthtech.com",
    "password": "password123",
    "firstName": "Nombre",
    "lastName": "Apellido",
    "phone": "+1234567890",
    "role": "PATIENT"
}
```
```http
POST /api/auth/login
Content-Type: application/json

{
    "email": "usuario@healthtech.com",
    "password": "password123"
}
```

### 📅 Gestión de Citas
```http
POST /api/appointments
Authorization: Bearer <token>
Content-Type: application/json

{
    "doctorId": 1,
    "patientId": 1,
    "appointmentDateTime": "2024-12-15T10:00:00",
    "type": "VIRTUAL",
    "reason": "Consulta general",
    "symptoms": "Dolor de cabeza"
}
```
```http
GET /api/appointments/patient/1
Authorization: Bearer <token>
```

```http
GET /api/appointments/doctor/1
Authorization: Bearer <token>
```

```http
PUT /api/appointments/1/status?status=CONFIRMED
Authorization: Bearer <token>
```

```http
DELETE /api/appointments/1
Authorization: Bearer <token>
```

### 🎥 Videollamadas
```http
POST /api/video-call/1/start
Authorization: Bearer <token>
```

```http
POST /api/video-call/meeting-123/end
Authorization: Bearer <token>
```

```http
GET /api/video-call/meeting-123/chat/history
Authorization: Bearer <token>
```

### 🩺 Doctores
```http
GET /api/doctors
Authorization: Bearer <token>
```

```http
GET /api/doctors/1
Authorization: Bearer <token>
```

```http
GET /api/doctors/specialization/Cardiología
Authorization: Bearer <token>
```

---

## 🛠️ Instalación y Configuración

### 🔧 Prerrequisitos
- Java 17+  
- MySQL 8.0+  
- Gradle  

### ⚙️ Pasos
```bash
git clone <repository-url>
cd teleasistencia-mvp
```

#### 1️⃣ Configurar base de datos MySQL
```sql
CREATE DATABASE teleasistencia;
```

#### 2️⃣ Configurar aplicación
Editar `src/main/resources/application-dev.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/teleasistencia
    username: root
    password: tu_password
```

#### 3️⃣ Ejecutar
```bash
./gradlew bootRun
```

#### 4️⃣ Probar
- API: http://localhost:8080  
- Health Check: GET http://localhost:8080/api/auth/health

---

## 🧪 Testing con Postman

Flujo completo de prueba:

1. **Registrar usuario**
```bash
POST http://localhost:8080/api/auth/register
```
2. **Login (guardar token JWT)**
```bash
POST http://localhost:8080/api/auth/login
```
3. **Crear cita**
```bash
POST http://localhost:8080/api/appointments
Authorization: Bearer <token>
```
4. **Iniciar videollamada**
```bash
POST http://localhost:8080/api/video-call/1/start
Authorization: Bearer <token>
```

---

## 📧 Configuración de Email

1. **Habilitar App Password en Gmail**
   - Activar verificación en 2 pasos
   - Generar contraseña de aplicación para “Mail”

2. **Configurar en application.yml**
```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: tu_email@gmail.com
    password: tu_app_password_16_chars
```

---

## 🔧 Estructura del Proyecto

```
src/main/java/com/healthtech/
├── config/
│   ├── SecurityConfig.java
│   ├── WebSocketConfig.java
│   └── CorsConfig.java
├── controller/
│   ├── AuthController.java
│   ├── AppointmentController.java
│   ├── VideoCallController.java
│   └── WebSocketController.java
├── entity/
│   ├── User.java
│   ├── Patient.java
│   ├── Doctor.java
│   ├── Appointment.java
│   └── MedicalRecord.java
├── repository/
│   ├── UserRepository.java
│   ├── AppointmentRepository.java
│   └── DoctorRepository.java
├── service/
│   ├── AuthService.java
│   ├── AppointmentService.java
│   ├── VideoCallService.java
│   └── EmailService.java
├── security/
│   ├── JwtUtil.java
│   ├── JwtRequestFilter.java
│   └── CustomUserDetailsService.java
└── dto/
    ├── LoginRequest.java
    ├── LoginResponse.java
    ├── AppointmentRequest.java
    └── AppointmentResponse.java
```

---

## 🎯 Tecnologías Utilizadas
- Spring Boot 3.2.0  
- Spring Security 6  
- JWT  
- MySQL / H2  
- WebSocket  
- JavaMail  
- Gradle  
- Lombok  

---

## 🚀 Despliegue en Producción

```bash
./gradlew clean build
```

Configurar `application-prod.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://servidor:3306/teleasistencia
    username: usuario_prod
    password: password_seguro
  jpa:
    hibernate:
      ddl-auto: update
```

Ejecutar:
```bash
java -jar build/libs/teleasistencia-mvp.jar
```

---

## 📞 Soporte
- Revisar documentación de APIs  
- Verificar logs de la aplicación  
- Probar con Postman usando los ejemplos proporcionados  

---

## ✅ Estado del Proyecto
**MVP COMPLETADO ✅**  
- Backend 100% funcional  
- APIs documentadas  
- Seguridad implementada  
- Base de datos configurada  
- Emails automáticos  
- Videollamadas integradas  
- Frontend React (Próxima fase)
