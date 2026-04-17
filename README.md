

# 📌 Plataforma de Reservas de Consultorios Médicos Universitarios

## 🧩 Descripción del Proyecto

API REST desarrollada para gestionar el sistema de reservas de consultorios médicos dentro de una universidad. Permite administrar pacientes, doctores, especialidades, consultorios, tipos de cita, horarios y citas médicas, garantizando control de disponibilidad, validaciones de negocio y trazabilidad.

---

## 🏗️ Arquitectura

El sistema sigue una **arquitectura por capas**:

* **Controller** → Manejo de endpoints REST
* **Service** → Lógica de negocio y validaciones
* **Repository** → Acceso a datos (JPA / JPQL)
* **Entity** → Modelo de datos
* **DTO + Mapper** → Transferencia de datos

---

## ⚙️ Stack Tecnológico

* Java 21
* Spring Boot 4
* PostgreSQL
* JPA / Hibernate
* Testcontainers
* JUnit 5
* Mockito
* Maven

---

## 📊 Modelo de Dominio

### Entidades principales

* Patient
* Specialty
* Doctor
* Office
* AppointmentType
* DoctorSchedule
* Appointment

### Relaciones clave

* Un doctor pertenece a una especialidad
* Un doctor tiene múltiples horarios
* Un paciente tiene múltiples citas
* Un consultorio tiene múltiples citas
* Un tipo de cita define la duración

---

## 📌 Reglas de Negocio

### 🧾 Creación de citas

* No se permite crear citas con:

  * Paciente inexistente o inactivo
  * Doctor inexistente o inactivo
  * Consultorio inexistente o inactivo
* No se permiten citas en el pasado
* La cita debe estar dentro del horario del doctor
* `endAt` se calcula automáticamente según duración
* No se permiten traslapes:

  * Doctor
  * Consultorio
  * Paciente
* Estado inicial obligatorio: **SCHEDULED**

---

### ✅ Confirmación

* Solo citas en estado **SCHEDULED** pueden confirmarse
* No se pueden confirmar citas canceladas, completadas o NO_SHOW
* Se debe mantener trazabilidad del cambio

---

### ❌ Cancelación

* Solo citas en estado **SCHEDULED** o **CONFIRMED**
* Requiere motivo obligatorio
* Libera disponibilidad inmediatamente

---

### ✔️ Finalización

* Solo citas **CONFIRMED** pueden completarse
* No se puede completar antes de la hora de inicio
* Permite registrar observaciones

---

### 🚫 No asistencia (NO_SHOW)

* Solo citas **CONFIRMED**
* No antes de la hora programada
* Se contabiliza para reportes

---

### 📊 Disponibilidad y reportes

* Dependen de:

  * Horarios del doctor
  * Citas existentes
  * Duración del tipo de cita
* Los slots deben ser completos (no parciales)
* Reportes obligatorios:

  * Ocupación de consultorios
  * Productividad de doctores
  * Pacientes con más inasistencias

---

## 🌐 Endpoints principales

### Pacientes

* POST /api/patients
* GET /api/patients
* GET /api/patients/{id}
* PUT /api/patients/{id}

### Doctores

* POST /api/doctors
* GET /api/doctors
* GET /api/doctors/{id}
* PUT /api/doctors/{id}

### Citas

* POST /api/appointments
* GET /api/appointments
* GET /api/appointments/{id}
* PUT /confirm
* PUT /cancel
* PUT /complete
* PUT /no-show

### Disponibilidad y reportes

* GET /api/availability/doctors/{doctorId}
* GET /api/reports/office-occupancy
* GET /api/reports/doctor-productivity
* GET /api/reports/no-show-patients

---

## 🧠 Decisiones de Diseño

### 1. Separación por capas

Permite:

* Escalabilidad
* Testeo independiente
* Mantenibilidad

---

### 2. Uso de DTOs

* Evita exponer entidades directamente
* Controla entrada/salida de datos
* Facilita validaciones

---

### 3. Uso de MapStruct

* Mapeo automático entre DTO ↔ Entity
* Reduce código repetitivo

---

### 4. Manejo de estados (Enum)

Se utiliza `AppointmentStatus` para controlar el flujo:

* SCHEDULED → CONFIRMED → COMPLETED / NO_SHOW
* CANCELLED en cualquier punto permitido

---

### 5. Validaciones en Service

Toda la lógica crítica se implementa en Service:

* Reglas de negocio
* Validación de traslapes
* Control de estados

---

### 6. Manejo de excepciones global

Clases:

* ResourceNotFoundException
* BusinessException
* ConflictException
* ValidationException

Centralizadas en:

* GlobalExceptionHandler

---

## 🧪 Estrategia de Testing

### Repository

* Pruebas de integración con Testcontainers
* Validación de consultas JPQL

### Service

* Pruebas unitarias con Mockito
* Validación de reglas de negocio

### Controller

* Pruebas con MockMvc
* Validación de respuestas HTTP

---

## 🚀 Ejecución del Proyecto

### 1. Clonar repositorio

```bash
git clone <repo-url>
cd proyecto
```

### 2. Configurar base de datos

Crear base de datos en PostgreSQL y configurar:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/db_name
spring.datasource.username=usuario
spring.datasource.password=password
```

---

### 3. Ejecutar aplicación

```bash
mvn clean install
mvn spring-boot:run
```

---

### 4. Ejecutar pruebas

```bash
mvn test
```

---

## 📦 Entregables esperados

* Código fuente organizado por capas
* Pruebas automatizadas funcionando
* Diagrama entidad-relación
* README técnico
* Colección de endpoints

---

## ✅ Consideraciones finales

* El sistema prioriza **consistencia y validación de reglas reales**
* Se evita lógica en controllers
* Se garantiza integridad mediante validaciones estrictas

---

