package co.edu.uniquindio.proyecto.infrastructure.jpa;

import co.edu.uniquindio.proyecto.domain.valueobject.PrioridadSolicitud.Nivel;  // ← CLAVE
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudJpaRepository extends JpaRepository<SolicitudEntity, Long> {

    List<SolicitudEntity> findByEstado(SolicitudEntity.EstadoSolicitudEnum estado);
    Optional<SolicitudEntity> findByCodigo(String codigo);
    List<SolicitudEntity> findBySolicitanteId(Long solicitanteId);

    List<SolicitudEntity> findByEstadoAndPrioridad_Nivel(
            SolicitudEntity.EstadoSolicitudEnum estado,
            Nivel nivel  // ✅ IMPORTADO
    );

    long countByEstado(SolicitudEntity.EstadoSolicitudEnum estado);

    @Query("SELECT s FROM SolicitudEntity s WHERE s.estado IN ('REGISTRADA', 'CLASIFICADA')")
    List<SolicitudEntity> findSolicitudesPendientesDeAsignacion();

    List<SolicitudEntity> findByCanalOrigen(SolicitudEntity.CanalOrigenEnum canalOrigen);
}