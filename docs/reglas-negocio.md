# 📘 Documento de Reglas de Negocio

## Sistema de Triage y Gestión de Solicitudes Académicas

---

## 🎯 Propósito

Este documento describe las reglas de negocio que gobiernan el comportamiento del dominio, indicando:

* Acción regulada
  * Condición obligatoria
* RF asociado
* Ubicación en el código

---

# 🔷 RN-01 — Registro obligatorio con estado inicial

**Acción que regula:**
Creación de una solicitud.

**Condición:**
Toda solicitud debe crearse en estado `REGISTRADA`.

**RF asociado:**
RF-01 — Registro de solicitudes
RF-04 — Gestión del ciclo de vida

**Vive en:**
Constructor del agregado `Solicitud`

**Validado por prueba:**
Debe verificar que el estado inicial sea REGISTRADA.

---

# 🔷 RN-02 — Transiciones de estado controladas

**Acción que regula:**
Cambio de estado de una solicitud.

**Condición:**
Solo se permiten las siguientes transiciones:

```
REGISTRADA → CLASIFICADA
CLASIFICADA → EN_ATENCION
EN_ATENCION → ATENDIDA
ATENDIDA → CERRADA
```

Cualquier otra transición debe generar excepción.

**RF asociado:**
RF-04 — Gestión del ciclo de vida

**Vive en:**
Métodos `clasificar()`, `atender()`, `marcarAtendida()`, `cerrar()` en `Solicitud`

**Validado por prueba:**
Pruebas de transición válida e inválida.

---

# 🔷 RN-03 — No se puede cerrar sin estar atendida

**Acción que regula:**
Cierre de solicitud.

**Condición:**
Solo puede cerrarse si el estado actual es `ATENDIDA`.

**RF asociado:**
RF-08 — Cierre de solicitudes

**Vive en:**
Método `cerrar()` en `Solicitud`

**Validado por prueba:**
Debe lanzar excepción si se intenta cerrar antes.

---

# 🔷 RN-04 — Una solicitud cerrada no puede modificarse

**Acción que regula:**
Cualquier operación de cambio (clasificar, priorizar, atender).

**Condición:**
Si estado = `CERRADA`, no se permite ninguna modificación.

**RF asociado:**
RF-08 — Reglas de cierre

**Vive en:**
Validación interna del agregado `Solicitud`

**Validado por prueba:**
Intento de modificación tras cierre debe fallar.

---

# 🔷 RN-05 — Toda acción relevante debe generar un evento en el historial

**Acción que regula:**
Cambios de estado o prioridad.

**Condición:**
Cada acción debe crear un `EventoHistorial`.

**RF asociado:**
RF-06 — Historial auditable

**Vive en:**
Método interno `registrarEvento()` dentro del agregado

**Validado por prueba:**
El tamaño del historial debe aumentar tras cada acción.

---

# 🔷 RN-06 — El historial no puede modificarse externamente

**Acción que regula:**
Acceso al historial.

**Condición:**
Debe exponerse como colección inmodificable.

**RF asociado:**
RF-06 — Historial auditable

**Vive en:**
Método `obtenerHistorial()` del agregado

**Validado por prueba:**
Intentar modificar la lista debe lanzar excepción.

---

# 🔷 RN-07 — Prioridad requiere justificación obligatoria

**Acción que regula:**
Asignación de prioridad.

**Condición:**
Toda prioridad debe incluir una justificación no vacía.

**RF asociado:**
RF-03 — Priorización de solicitudes

**Vive en:**
Constructor del Value Object `PrioridadSolicitud`

**Validado por prueba:**
Debe lanzar excepción si no hay justificación.

---

# 🔷 RN-08 — Email debe tener formato válido

**Acción que regula:**
Creación de usuario.

**Condición:**
Email debe cumplir patrón válido.

**RF asociado:**
RF-13 — Definición de roles (usuarios activos válidos)

**Vive en:**
Constructor de `Email`

**Validado por prueba:**
Email inválido debe lanzar excepción.

---

# 🔷 RN-09 — IdUsuario debe ser numérico

**Acción que regula:**
Creación de usuario.

**Condición:**
El identificador solo puede contener números.

**RF asociado:**
RF-13 — Definición de roles

**Vive en:**
Constructor de `IdUsuario`

**Validado por prueba:**
Debe fallar si contiene caracteres no numéricos.

---

# 🔷 RN-10 — Descripción debe tener contenido significativo

**Acción que regula:**
Registro de solicitud.

**Condición:**
La descripción no puede ser nula ni demasiado corta.

**RF asociado:**
RF-01 — Registro de solicitudes

**Vive en:**
Constructor de `DescripcionSolicitud`

**Validado por prueba:**
Debe lanzar excepción si es inválida.

---

# 🔷 RN-11 — El sistema debe funcionar sin IA

**Acción que regula:**
Diseño del sistema.

**Condición:**
La clasificación automática debe ser desacoplada del dominio.

**RF asociado:**
RF-11 — Independencia de IA

**Vive en:**
Diseño arquitectónico (interfaz opcional en domain.service)

**Validado por:**
Revisión de diseño (no por prueba)

---

# 🔷 RN-12 — Usuario inicia activo

**Acción que regula:**
Creación de usuario.

**Condición:**
Todo usuario inicia en estado ACTIVO.

**RF asociado:**
RF-13 — Definición de roles

**Vive en:**
Constructor de `Usuario`

**Validado por prueba:**
Debe verificarse estado inicial.

---

## 🔷 RN-13 — Solo funcionario activo puede gestionar solicitudes

**Acción que regula:**
Clasificación, priorización, atención y cierre de solicitudes.

**Condición:**
Solo un usuario de tipo FUNCIONARIO y en estado ACTIVO puede ejecutar estas acciones.
Si no cumple, debe lanzarse excepción.

**RF asociado:**
RF-13 — Definición de roles
RF-04 — Gestión del ciclo de vida

**Vive en:**
Método privado validarFuncionario() del agregado Solicitud

Validado por prueba:
Debe lanzar RolNoAutorizadoException si:

El usuario no es funcionario.

El funcionario está inactivo.

---

## 🔷 RN-14 — Solo estudiante puede registrar solicitudes

**Acción que regula:**
Creación de una solicitud.

**Condición:**
Solo un usuario de tipo ESTUDIANTE puede crear una solicitud.
Si no cumple, debe lanzarse excepción.

**RF asociado:**
RF-01 — Registro de solicitudes
RF-13 — Definición de roles

**Vive en:**
Constructor del agregado Solicitud

**Validado por prueba:**
Debe lanzar RolNoAutorizadoException si un usuario distinto a ESTUDIANTE intenta crearla.

---

## 🔷 RN-15 — Funcionario debe estar activo

**Acción que regula:**
Ejecución de acciones sobre una solicitud.

**Condición:**
El funcionario debe estar en estado ACTIVO.

**RF asociado:**
RF-13 — Definición de roles

**Vive en:**
Validación interna en validarFuncionario() dentro del agregado.

**Validado por prueba:**
Intentar gestionar con funcionario inactivo debe lanzar excepción.

## 🔷 RN-16 — Código de solicitud es autogenerado

---

**Acción que regula:**
Creación de una solicitud.

**Condición:**
El código (CodigoSolicitud) debe generarse automáticamente y no puede ser nulo ni asignado manualmente.

**RF asociado:**
RF-01 — Registro de solicitudes

**Vive en:**
Constructor del agregado Solicitud
Método CodigoSolicitud.generar()

**Validado por prueba:**
El código no debe ser nulo tras la creación.

---

## 🔷 RN-17 — Estado e historial deben mantenerse sincronizados

**Acción que regula:**
Cambio de estado de la solicitud.

**Condición:**
Cada transición válida debe generar un EventoHistorial que refleje el nuevo estado.

**RF asociado:**
RF-06 — Historial auditable
RF-04 — Gestión del ciclo de vida

**Vive en:**
Métodos del agregado Solicitud

**Validado por prueba:**
Cada cambio incrementa el tamaño del historial.
---

## 🔷 RN-18 — Solicitud requiere tipo y canal

**Acción que regula:**
Registro de solicitud.

**Condición:**
Toda solicitud debe tener definido un `TipoSolicitud` y un `TipoCanal`.
Si alguno es nulo, debe rechazarse el registro.

**RF asociado:**
RF-01 — Registro de solicitudes

**Vive en:**
Constructor del agregado `Solicitud`

**Validado por prueba:**
Debe lanzar excepción si tipo o canal son nulos.
