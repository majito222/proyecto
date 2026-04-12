// infrastructure/jpa/SolicitudEntity.java
package co.edu.uniquindio.proyecto.infrastructure.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA para Solicitud - Mapea Aggregate Root del dominio
 */
@Entity
@Table(name = "solicitudes", indexes = {
        @Index(name = "idx_estado", columnList = "estado"),
        @Index(name = "idx_codigo", columnList = "codigo", unique = true),
        @Index(name = "idx_solicitante", columnList = "solicitante_id")
})
@Getter @Setter @NoArgsConstructor
public class SolicitudEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", unique = true, nullable = false, length = 20)
    private String codigo;

    @Column(name = "tipo_solicitud_id", nullable = false)
    private Long tipoSolicitudId;

    @Column(name = "descripcion", nullable = false, length = 1000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoSolicitudEnum estado;

    // ✅ Usa TU PrioridadEmbeddable
    @Embedded
    private PrioridadEmbeddable prioridad;

    @Column(name = "solicitante_id", nullable = false)
    private Long solicitanteId;

    @Column(name = "responsable_id")
    private Long responsableId;

    @Enumerated(EnumType.STRING)
    @Column(name = "canal_origen", length = 30)
    private CanalOrigenEnum canalOrigen;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Historial como ElementCollection (Value Objects)
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "eventos_historial", joinColumns = @JoinColumn(name = "solicitud_id"))
    @OrderColumn(name = "secuencia")
    private List<EventoHistorialEmbeddable> historial = new ArrayList<>();

    // Enums JPA (compatibles con domain)
    public enum EstadoSolicitudEnum {
        REGISTRADA, CLASIFICADA, EN_ATENCION, ATENDIDA, CERRADA
    }

    public enum CanalOrigenEnum {
        CSU, CORREO, SAC, TELEFONICO, PRESENCIAL, WEB
    }

    // Inner class para historial (Value Object)
    @Embeddable
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class EventoHistorialEmbeddable {
        @Column(name = "fecha_hora", nullable = false)
        private LocalDateTime fechaHora;

        @Column(name = "accion", nullable = false, length = 50)
        private String accion;

        @Column(name = "responsable_id")
        private Long responsableId;

        @Column(name = "observacion", length = 500)
        private String observacion;
    }
}