package co.edu.uniquindio.proyecto.infrastructure.jpa;

import co.edu.uniquindio.proyecto.domain.valueobject.PrioridadSolicitud.Nivel;  // ← COPIAR
import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PrioridadEmbeddable {
    @Enumerated(EnumType.STRING)
    private Nivel nivel;

    private String justificacion;
}