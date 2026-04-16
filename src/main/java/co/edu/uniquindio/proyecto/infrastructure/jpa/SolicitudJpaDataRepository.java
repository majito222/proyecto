package co.edu.uniquindio.proyecto.infrastructure.jpa;

import co.edu.uniquindio.proyecto.domain.valueobject.EstadoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.PrioridadSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoCanal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudJpaDataRepository extends JpaRepository<SolicitudEntity, Long> {

    List<SolicitudEntity> findByEstado(EstadoSolicitud estado);

    Optional<SolicitudEntity> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<SolicitudEntity> findBySolicitanteId(String solicitanteId);

    List<SolicitudEntity> findByEstadoAndPrioridad_Nivel(EstadoSolicitud estado, PrioridadSolicitud.Nivel nivel);

    List<SolicitudEntity> findByCodigoContainingIgnoreCaseOrDescripcionContainingIgnoreCase(String codigo, String descripcion);

    List<SolicitudEntity> findByEstadoOrderByPrioridad_NivelDesc(EstadoSolicitud estado);

    List<SolicitudEntity> findByCanalOrigenOrderByPrioridad_NivelDesc(TipoCanal canalOrigen);

    Page<SolicitudEntity> findByEstado(EstadoSolicitud estado, Pageable pageable);

    Page<SolicitudEntity> findByCanalOrigen(TipoCanal canalOrigen, Pageable pageable);

    List<SolicitudEntity> findByEstadoIn(List<EstadoSolicitud> estados);

    long countByEstado(EstadoSolicitud estado);

    long countByCanalOrigen(TipoCanal canalOrigen);

    @Query("SELECT s FROM SolicitudEntity s WHERE s.estado IN ('REGISTRADA', 'CLASIFICADA')")
    List<SolicitudEntity> findSolicitudesPendientesDeAsignacion();

    @Query("SELECT s FROM SolicitudEntity s WHERE s.responsableId IS NULL AND s.prioridad.nivel = :nivel")
    List<SolicitudEntity> buscarSinAsignarPorPrioridadAlta(@Param("nivel") PrioridadSolicitud.Nivel nivel);

    @Query("SELECT s FROM SolicitudEntity s WHERE s.estado <> 'CERRADA'")
    Page<SolicitudEntity> buscarActivasPaginadas(Pageable pageable);

    @Query("""
            SELECT s FROM SolicitudEntity s
            WHERE (:estado IS NULL OR s.estado = :estado)
              AND (:canal IS NULL OR s.canalOrigen = :canal)
            """)
    Page<SolicitudEntity> buscarPorFiltros(
            @Param("estado") EstadoSolicitud estado,
            @Param("canal") TipoCanal canal,
            Pageable pageable
    );

    @Query("SELECT s FROM SolicitudEntity s LEFT JOIN FETCH s.historial WHERE s.codigo = :codigo")
    Optional<SolicitudEntity> buscarConHistorialPorCodigo(@Param("codigo") String codigo);

    @Query(value = "SELECT COUNT(*) as total, estado FROM solicitudes GROUP BY estado", nativeQuery = true)
    List<Object[]> reporteAgrupacionPorEstadoNativo();
}
