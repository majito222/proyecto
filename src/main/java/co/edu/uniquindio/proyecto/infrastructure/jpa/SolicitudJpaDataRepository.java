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

    List<SolicitudEntity> findByEstado(SolicitudEntity.EstadoSolicitudEnum estado);
    Optional<SolicitudEntity> findByCodigo(String codigo);
    List<SolicitudEntity> findByEstudianteId(String estudianteId);
    List<SolicitudEntity> findByEstadoAndPrioridad_Nivel(EstadoSolicitud estado, PrioridadSolicitud.Nivel nivel);
    long countByEstado(EstadoSolicitud estado);
    @Query("SELECT s FROM SolicitudEntity s WHERE s.estado IN ('REGISTRADA', 'CLASIFICADA')")
    List<SolicitudEntity> findSolicitudesPendientesDeAsignacion();
    List<SolicitudEntity> findByCanalOrigen(TipoCanal canalOrigen);

    List<SolicitudEntity> findByEstadoOrderByPrioridad_NivelDesc(SolicitudEntity.EstadoSolicitudEnum estado);
    List<SolicitudEntity> findByCanalOrigenOrderByPrioridad_NivelDesc(TipoCanal canalOrigen);

    @Query("SELECT s FROM SolicitudEntity s WHERE s.estado = ?1 ORDER BY s.prioridad.nivel DESC")
    List<SolicitudEntity> buscarPorEstadoPorPrioridadJPQL(EstadoSolicitud estado);

    @Query("SELECT s FROM SolicitudEntity s WHERE s.codigo LIKE %:codigo%")
    List<SolicitudEntity> buscarPorCodigo(@Param("codigo") String codigo);

    // Sin asignar + Prioridad alta
    @Query("SELECT s FROM SolicitudEntity s WHERE s.responsableId IS NULL AND s.prioridad.nivel = :nivel")
    List<SolicitudEntity> buscarSinAsignarPorPrioridadAlta(@Param("nivel") PrioridadSolicitud.Nivel nivel);

    //Paginación PURA
    @Query("SELECT s FROM SolicitudEntity s WHERE s.estado != 'CERRADA'")
    Page<SolicitudEntity> buscarActivasPaginadas(Pageable pageable);

    // Paginación con inferencia
    Page<SolicitudEntity> findByEstado(EstadoSolicitud estado, Pageable pageable);
    Page<SolicitudEntity> findByCanalOrigen(TipoCanal canalOrigen, Pageable pageable);

    // Historial (si existe el campo historial)
    @Query("SELECT s FROM SolicitudEntity s LEFT JOIN FETCH s.historial WHERE s.codigo = :codigo")
    Optional<SolicitudEntity> buscarConHistorialPorCodigo(@Param("codigo") String codigo);

    // Estados múltiples
    @Query("SELECT s FROM SolicitudEntity s WHERE s.estado IN (:estados)")
    List<SolicitudEntity> buscarPorVariosEstados(@Param("estados") List<SolicitudEntity.EstadoSolicitudEnum> estados);

    // Conteos adicionales
    long countByCanalOrigen(TipoCanal canalOrigen);

    // Native Query
    @Query(value = "SELECT COUNT(*) as total, estado FROM solicitudes GROUP BY estado",
            nativeQuery = true)
    List<Object[]> reporteAgrupacionPorEstadoNativo();
}