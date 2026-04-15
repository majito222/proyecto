package co.edu.uniquindio.proyecto.infrastructure.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventoHistorialEmbeddable {

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "accion", nullable = false, length = 150)
    private String accion;

    @Column(name = "responsable", nullable = false, length = 60)
    private String responsable;

    @Column(name = "observacion", length = 500)
    private String observacion;
}
