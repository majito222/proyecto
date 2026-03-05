# 📘 Sistema de Triage y Gestión de Solicitudes Académicas

## 👥 Integrantes

* Mariajose Valencia Diaz
* Santiago Marín Serna
* Juan Esteban Cuervo

Curso: Programación Avanzada

Programa: Ingeniería de Sistemas y Computación

Universidad del Quindío

---

# 🎯 Contexto del Proyecto

El Programa de Ingeniería de Sistemas y Computación gestiona múltiples solicitudes académicas provenientes de diferentes canales (CSU, correo, SAC, telefónico, entre otros). Actualmente, estas solicitudes carecen de una estructura unificada, mecanismos formales de clasificación y trazabilidad clara, generando ineficiencia operativa.

Este proyecto tiene como propósito diseñar y materializar el núcleo del dominio de un Sistema de Triage y Gestión de Solicitudes Académicas aplicando principios de:

* Arquitectura Empresarial
* Domain-Driven Design (DDD)
* Modelado de Dominio
* Pruebas Unitarias del Dominio

---

# 🏗 Arquitectura del Dominio

La solución implementa una arquitectura orientada al dominio con separación clara entre:

```
co.edu.uniquindio.proyecto
└── domain
    ├── entity
    ├── valueobject
    ├── exception
    └── service
```

No existen dependencias con infraestructura, bases de datos ni frameworks externos en la capa de dominio.

---

# 🧠 Modelo de Dominio

## 🔷 Agregado Raíz

### Solicitud

Responsable de:

* Gestionar el ciclo de vida
* Proteger invariantes
* Registrar historial auditable
* Controlar clasificación y priorización

Estados soportados:

```
REGISTRADA → CLASIFICADA → EN_ATENCION → ATENDIDA → CERRADA
```

---

## 🔷 Entidades

* Solicitud (Aggregate Root)
* Usuario
* EventoHistorial

---

## 🔷 Value Objects

* CodigoSolicitud
* IdUsuario
* Email
* DescripcionSolicitud
* PrioridadSolicitud
* TipoSolicitud
* TipoCanal
* EstadoSolicitud
* TipoUsuario
* EstadoUsuario

Todos los Value Objects son:

* Inmutables
* Auto-validados
* Comparables por valor

---

# 📜 Reglas de Negocio Implementadas

* No se puede realizar una transición de estado inválida.
* No se puede cerrar una solicitud si no está en estado ATENDIDA.
* Una solicitud cerrada no puede modificarse.
* Toda acción relevante genera un evento en el historial.
* El historial no puede modificarse externamente.
* Los Value Objects no pueden crearse en estado inválido.

Cada regla está encapsulada en el dominio y validada mediante pruebas unitarias.

---

# 🧪 Pruebas Unitarias

Se implementaron pruebas siguiendo el patrón AAA (Arrange, Act, Assert).

Cobertura:

* Validación de Value Objects
* Cambios de estado en entidades
* Invariantes del agregado
* Transiciones válidas e inválidas
* Protección contra modificación externa del historial

Todas las pruebas deben ejecutarse en verde.

---

# ▶️ Cómo ejecutar las pruebas

Desde la raíz del proyecto:

Windows:

```
gradlew test
```

Mac/Linux:

```
./gradlew test
```

O directamente desde IntelliJ ejecutando la carpeta `src/test/java`.

---

# 📂 Estructura de Entrega

* Código fuente en `src/main/java`
* Pruebas unitarias en `src/test/java`
* Documentación UML en carpeta `/docs`
* Glosario y reglas documentadas en `/docs`
* Este README como guía principal del proyecto

---

# 🚀 Estado Actual

Entrega 01 – Modelado del Dominio y Materialización en Código.

El dominio está completamente modelado, implementado y validado mediante pruebas unitarias, listo para evolucionar hacia las capas de aplicación, persistencia y presentación en las siguientes entregas.
