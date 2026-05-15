# 📘 Glosario de Lenguaje Ubicuo
## Sistema de Triage y Gestión de Solicitudes Académicas

Este documento define los conceptos fundamentales del dominio del sistema de triage y gestión de solicitudes académicas.

Su objetivo es establecer un **lenguaje ubicuo compartido** entre desarrolladores, analistas y stakeholders institucionales, permitiendo una comprensión consistente del dominio.

---

# 🔷 1. Solicitud

**Definición**

Representa una petición académica realizada por un estudiante que debe ser registrada, clasificada, priorizada, gestionada y eventualmente cerrada siguiendo un ciclo de vida definido.

**Clasificación**

Agregado Raíz (Aggregate Root)

**Responsabilidades**

- Controlar las reglas del dominio asociadas a la solicitud.
- Gestionar su propio ciclo de vida.
- Registrar los eventos relevantes en su historial.
- Mantener sincronía entre estado y eventos registrados.

**Reglas del dominio**

- Solo puede ser creada por un usuario de tipo **ESTUDIANTE**.
- Solo puede ser gestionada por usuarios autorizados según su rol.
- No puede modificarse si se encuentra en estado **CERRADA**.
- Toda acción válida genera un **EventoHistorial**.

---

# 🔷 2. Usuario

**Definición**

Persona que interactúa con el sistema y que puede registrar o gestionar solicitudes dependiendo de su rol.

**Clasificación**

Entidad

**Características**

- Posee una identidad única (`IdUsuario`).
- Tiene un rol dentro del sistema (`TipoUsuario`).
- Posee un estado operativo (`EstadoUsuario`).

El estado del usuario impacta las reglas del dominio, ya que algunas acciones solo pueden ser ejecutadas por usuarios activos.

---

# 🔷 3. EstadoUsuario

**Definición**

Representa la condición operativa de un usuario dentro del sistema.

**Clasificación**

Value Object implementado como **Enum en el código**.

**Valores definidos en el sistema**
- ACTIVO
- INACTIVO

**Regla de dominio**

Solo los usuarios en estado **ACTIVO** pueden ejecutar acciones dentro del sistema.

---

# 🔷 4. TipoUsuario

**Definición**

Define el rol operativo que posee un usuario dentro del sistema.

**Clasificación**

Value Object implementado como **Enum en el código**.

**Valores definidos en el sistema**
- ESTUDIANTE
- FUNCIONARIO
- ADMINISTRADOR 

**Reglas de dominio**

- Solo un **ESTUDIANTE** puede registrar solicitudes.
- Solo un **ADMINISTRADOR** puede clasificar, priorizar y cerrar solicitudes.
- Solo un **FUNCIONARIO** puede atender solicitudes.

---

# 🔷 5. Estudiante

**Definición**

Usuario autorizado para registrar solicitudes académicas dentro del sistema.

**Clasificación**

Subtipo conceptual de Usuario.

**Responsabilidad principal**

- Registrar nuevas solicitudes.

---

# 🔷 6. Funcionario

**Definición**

Usuario encargado de atender solicitudes académicas previamente clasificadas.

**Clasificación**

Subtipo conceptual de Usuario.

**Responsabilidad principal**

- Atender solicitudes.

**Regla de dominio**

Debe encontrarse en estado **ACTIVO** para poder ejecutar esta acción.

---

# 🔷 7. Administrador

**Definición**

Usuario responsable de la gestión administrativa de las solicitudes dentro del sistema.

**Clasificación**

Subtipo conceptual de Usuario.

**Responsabilidades**

- Clasificar solicitudes.
- Priorizar solicitudes.
- Cerrar solicitudes.

---

# 🔷 8. CodigoSolicitud

**Definición**

Identificador único que distingue una solicitud dentro del sistema.

**Clasificación**

Value Object

**Reglas asociadas**

- No puede ser nulo.
- No puede ser vacío.
- La igualdad se determina por valor.

---

# 🔷 9. IdUsuario

**Definición**

Identificador único que representa a un usuario dentro del sistema.

**Clasificación**

Value Object

**Reglas asociadas**

- Solo puede contener caracteres numéricos.
- No puede ser nulo.
- No puede ser vacío.

---

# 🔷 10. Email

**Definición**

Dirección de correo electrónico asociada a un usuario para comunicación institucional.

**Clasificación**

Value Object

**Reglas asociadas**

- Debe tener formato válido.
- Es inmutable.
- La igualdad se determina por valor.

---

# 🔷 11. DescripcionSolicitud

**Definición**

Texto que describe el motivo o contenido de una solicitud académica.

**Clasificación**

Value Object

**Reglas asociadas**

- No puede ser nula.
- Debe tener una longitud mínima válida.

---

# 🔷 12. TipoSolicitud

**Definición**

Categoría académica a la cual pertenece una solicitud.

**Clasificación**

Value Object implementado como **Enum en el código**.

**Valores definidos en el sistema**
- REGISTRO DE ASIGNATURA
- HOMOLOGACION
- CANCELACION
- SOLICITUD DE CUPO
- CONSULTAACADEMICA

---

# 🔷 13. TipoCanal

**Definición**

Medio a través del cual se registra una solicitud en el sistema.

**Clasificación**

Value Object implementado como **Enum en el código**.

**Valores definidos en el sistema**
- CSU
- CORREO
- SAC
- TELEFONICO

---

# 🔷 14. EstadoSolicitud

**Definición**

Representa la etapa actual dentro del ciclo de vida de una solicitud.

**Clasificación**

Value Object implementado como **Enum en el código**.

**Estados definidos en el sistema**
- REGISTRADA
- CLASIFICADA 
- PRIORIZADA
- EN ATENCION
-  ATENDIDA
-  CERRADA
-  CANCELADA

**Regla de dominio**

Las solicitudes deben seguir el flujo de estados definido.

---

# 🔷 15. PrioridadSolicitud

**Definición**

Nivel de urgencia asignado a una solicitud junto con una justificación.

**Clasificación**

Value Object

**Componentes**

- Nivel de prioridad
- Justificación

**Reglas asociadas**

- Debe tener un nivel definido.
- Debe incluir una justificación obligatoria.

---

# 🔷 16. EventoHistorial

**Definición**

Registro auditable que documenta cada acción relevante realizada sobre una solicitud.

**Clasificación**

Entidad interna del agregado **Solicitud**.

**Características**

- No posee identidad global fuera del agregado.
- Solo puede crearse desde la entidad **Solicitud**.

**Reglas**

- Toda acción relevante genera un evento.
- No puede modificarse externamente.

---

# 🔷 17. Historial de Solicitud

**Definición**

Colección ordenada de eventos que documentan la evolución de una solicitud desde su creación hasta su cierre.

**Clasificación**

Componente interno del agregado Solicitud.

**Propósito**

- Garantizar trazabilidad de acciones.
- Mantener auditoría del ciclo de vida.

---

# 🔷 18. Servicio de Dominio de Solicitudes

**Definición**

Componente encargado de coordinar operaciones del dominio que involucran múltiples entidades.

**Clasificación**

Servicio de Dominio.

**Ejemplo en el sistema**
- SolicitudService 

**Responsabilidades**

- Registrar solicitudes.
- Coordinar acciones entre Usuario y Solicitud.
- Validar permisos del usuario antes de ejecutar acciones del dominio.

---

# 🔷 Acciones del Dominio

## Clasificación

Acción mediante la cual un **administrador** determina el tipo formal de una solicitud registrada.

---

## Priorización

Asignación de un **nivel de urgencia** a una solicitud, acompañada de una justificación obligatoria.

---

## Atención

Proceso mediante el cual un **funcionario** gestiona una solicitud con el objetivo de resolverla.

---

## Cierre

Acción final ejecutada por un **administrador** que concluye el ciclo de vida de la solicitud.

---

# 🔷 Invariantes del Agregado Solicitud

El agregado **Solicitud** debe cumplir las siguientes reglas del dominio:

- El estado y el historial deben mantenerse sincronizados.
- No puede modificarse si está en estado **CERRADA**.
- Las transiciones de estado deben seguir el flujo definido.
- Toda acción válida debe generar un **EventoHistorial**.
- Solo un **ESTUDIANTE** puede crear solicitudes.
- Solo un **ADMINISTRADOR ACTIVO** puede clasificar, priorizar o cerrar solicitudes.
- Solo un **FUNCIONARIO ACTIVO** puede atender solicitudes.