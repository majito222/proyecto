package co.edu.uniquindio.proyecto.infrastructure.jpa;

import co.edu.uniquindio.proyecto.domain.valueobject.EstadoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.PrioridadSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoCanal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SolicitudJpaDataRepository extends JpaRepository<SolicitudEntity, Long> {

    List<SolicitudEntity> findByEstado(EstadoSolicitud estado);

    Optional<SolicitudEntity> findByCodigo(String codigo);

    List<SolicitudEntity> findByEstudianteId(String estudianteId);

    List<SolicitudEntity> findByEstadoAndPrioridad_Nivel(
            EstadoSolicitud estado,
            PrioridadSolicitud.Nivel nivel
    );

    long countByEstado(EstadoSolicitud estado);

    @Query("SELECT s FROM SolicitudEntity s WHERE s.estado IN ('REGISTRADA', 'CLASIFICADA')")
    List<SolicitudEntity> findSolicitudesPendientesDeAsignacion();

    List<SolicitudEntity> findByCanalOrigen(TipoCanal canalOrigen);
}
