# 📘 Glosario de Lenguaje Ubicuo

## Sistema de Triage y Gestión de Solicitudes Académicas

Este documento define los términos fundamentales del dominio, asegurando un lenguaje compartido entre desarrolladores, analistas y stakeholders institucionales.

---

## 🔷 1. Solicitud

**Definición:**
Representa una petición académica realizada por un usuario del sistema que debe ser registrada, clasificada, priorizada, gestionada y eventualmente cerrada siguiendo un ciclo de vida definido.

**Clasificación:**
Agregado (Agregado Raíz)

**Observaciones:**

* Controla sus invariantes.
* Gestiona su propio historial.
* Es el punto de acceso principal al dominio.
  
**Invariantes adicionales:**

- Solo puede ser creada por un usuario de tipo ESTUDIANTE.
- Solo puede ser gestionada por un usuario de tipo FUNCIONARIO ACTIVO.
- No puede modificarse si está en estado CERRADA.
- Toda acción válida genera un EventoHistorial.

---

## 🔷 2. Usuario

**Definición:**
Persona que interactúa con el sistema. Puede registrar solicitudes o gestionarlas según su rol.

**Clasificación:**
Entidad

**Observaciones:**
Posee identidad propia, rol (TipoUsuario) y estado operativo (EstadoUsuario).

Su estado impacta reglas del dominio, ya que solo un funcionario ACTIVO puede ejecutar acciones sobre solicitudes.

---

## 🔷 2.1 EstadoUsuario

**Definición:**
Representa la condición operativa del usuario dentro del sistema.

**Clasificación:**
Value Object (Enum)

**Valores:**

- ACTIVO
- INACTIVO

**Regla Asociada:**

- Solo un FUNCIONARIO en estado ACTIVO puede gestionar solicitudes.

---

## 🔷 3. Estudiante

**Definición:**
Tipo de usuario que puede registrar solicitudes académicas.

**Clasificación:**
Entidad (subtipo conceptual de Usuario)

---

## 🔷 4. Funcionario

**Definición:**
Tipo de usuario encargado de clasificar, atender o cerrar solicitudes.

**Clasificación:**
Entidad (subtipo conceptual de Usuario)

---

## 🔷 5. CodigoSolicitud

**Definición:**
Identificador único que distingue una solicitud dentro del sistema.

**Clasificación:**
Value Object

**Reglas Asociadas:**

* No puede ser nulo ni vacío.
* Se compara por valor.

---

## 🔷 6. IdUsuario

**Definición:**
Identificador único del usuario dentro del sistema.

**Clasificación:**
Value Object

**Reglas Asociadas:**

* Solo puede contener caracteres numéricos.
* No puede ser nulo ni vacío.

---

## 🔷 7. Email

**Definición:**
Dirección electrónica asociada a un usuario para comunicación institucional.

**Clasificación:**
Value Object

**Reglas Asociadas:**

* Debe tener formato válido.
* Es inmutable.
* Igualdad por valor.

---

## 🔷 8. DescripcionSolicitud

**Definición:**
Detalle textual que explica el motivo o contenido de la solicitud.

**Clasificación:**
Value Object

**Reglas Asociadas:**

* No puede ser nula.
* Debe tener una longitud mínima válida.

---

## 🔷 9. TipoSolicitud

**Definición:**
Categoría académica a la cual pertenece la solicitud (ej. certificación, homologación, revisión de nota).

**Clasificación:**
Value Object (Enum del dominio)

---

## 🔷 10. TipoCanal

**Definición:**
Medio por el cual fue registrada la solicitud (ej. web, presencial, telefónico).

**Clasificación:**
Value Object (Enum del dominio)

---

## 🔷 11. EstadoSolicitud

**Definición:**
Representa la etapa actual del ciclo de vida de la solicitud.

**Clasificación:**
Value Object (Enum del dominio)

**Estados Definidos:**

* REGISTRADA
* CLASIFICADA
* EN_ATENCION
* ATENDIDA
* CERRADA

---

## 🔷 12. PrioridadSolicitud

**Definición:**
Nivel de urgencia asignado a una solicitud con su respectiva justificación.

**Clasificación:**
Value Object

**Reglas Asociadas:**

* Debe tener un nivel definido.
* Debe incluir justificación obligatoria.

---

## 🔷 13. EventoHistorial

**Definición:**
Registro auditable que documenta cada acción relevante realizada sobre una solicitud.

**Clasificación:**
Entidad interna del agregado (Entidad dependiente sin identidad global)

**Reglas Asociadas:**

* Toda acción relevante genera un evento.
* No puede modificarse externamente.

---

## 🔷 14. Historial de Solicitud

**Definición:**
Colección ordenada de eventos que reflejan la evolución de una solicitud desde su creación hasta su cierre.

**Clasificación:**
Componente interno del Agregado

---

## 🔷 15. TipoUsuario

**Definición:**
Define el rol operativo del usuario dentro del sistema.

**Clasificación:**
Value Object (Enum)

**Valores:**

- ESTUDIANTE
- FUNCIONARIO
- ADMINISTRADOR

**Reglas Asociadas:**

- Solo un ESTUDIANTE puede registrar solicitudes.
- Solo un FUNCIONARIO puede clasificar, priorizar, atender o cerrar solicitudes.

---

## 🔷 16. Servicio de Sugerencia IA (Diseño futuro)

**Definición:**
Componente externo que puede sugerir clasificación o prioridad basada en análisis automatizado.

**Clasificación:**
Servicio de Dominio (interfaz conceptual)

**Nota de Diseño:**
El sistema debe funcionar independientemente de este servicio (RF-11).

---

## 🔷 Clasificación

Acción mediante la cual un funcionario determina el tipo formal de una solicitud registrada.

Clasificación: Acción de dominio.

## 🔷 Priorización

Asignación de nivel de urgencia a una solicitud, con justificación obligatoria.

## 🔷 Atención

Proceso mediante el cual un funcionario gestiona la solicitud hasta resolverla.

## 🔷 Cierre

Acción final que concluye el ciclo de vida de la solicitud.

## 🔷 Invariante del Agregado

- El estado y el historial deben mantenerse sincronizados.
- No puede modificarse si está en estado CERRADA.
- Las transiciones de estado deben seguir el flujo definido.
- Toda acción válida debe generar un EventoHistorial.
- Solo un ESTUDIANTE puede crear una solicitud.
- Solo un FUNCIONARIO ACTIVO puede gestionar una solicitud.

