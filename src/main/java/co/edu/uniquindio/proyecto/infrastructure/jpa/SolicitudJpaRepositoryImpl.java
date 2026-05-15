package co.edu.uniquindio.proyecto.infrastructure.jpa;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.CodigoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.EstadoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.PrioridadSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoCanal;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class SolicitudJpaRepositoryImpl implements SolicitudRepository {

    private final SolicitudJpaDataRepository jpaRepository;
    private final SolicitudPersistenceMapper mapper;

    public SolicitudJpaRepositoryImpl(SolicitudJpaDataRepository jpaRepository,
                                      SolicitudPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Solicitud guardar(Solicitud solicitud) {
        SolicitudEntity entity = mapper.toEntity(solicitud);
        jpaRepository.findByCodigo(solicitud.getCodigo().valor()).ifPresent(existing -> entity.setId(existing.getId()));
        SolicitudEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Solicitud buscarPorCodigo(CodigoSolicitud codigo) {
        return jpaRepository.findByCodigo(codigo.valor())
                .map(mapper::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud no encontrada: " + codigo.valor()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Solicitud> buscarPorEstado(EstadoSolicitud estado) {
        return jpaRepository.findByEstado(estado).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Solicitud> buscarPorEstudiante(IdUsuario id) {
        return jpaRepository.findBySolicitanteId(id.valor()).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Solicitud> listarTodas() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void eliminarPorCodigo(CodigoSolicitud codigo) {
        jpaRepository.findByCodigo(codigo.valor()).ifPresent(jpaRepository::delete);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(CodigoSolicitud codigo) {
        return jpaRepository.existsByCodigo(codigo.valor());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Solicitud> buscarPorEstadoPrioridad(EstadoSolicitud estado, Pageable pageable) {
        return jpaRepository.findByEstadoOrderByPrioridad_NivelDesc(estado, pageable).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Solicitud> buscarSinAsignarAltaPrioridad(PrioridadSolicitud.Nivel nivel, Pageable pageable) {
        return jpaRepository.buscarSinAsignarPorPrioridadAlta(nivel, pageable).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Solicitud> buscarPorCodigoParcial(String codigoParcial) {
        return jpaRepository.findByCodigoContainingIgnoreCaseOrDescripcionContainingIgnoreCase(codigoParcial, codigoParcial)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Solicitud> buscarPorTexto(String texto, Pageable pageable) {
        return jpaRepository
                .findByCodigoContainingIgnoreCaseOrDescripcionContainingIgnoreCase(texto, texto, pageable)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Solicitud> buscarActivasPaginadas(Pageable pageable) {
        return jpaRepository.buscarActivasPaginadas(pageable).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Solicitud> buscarPorFiltros(EstadoSolicitud estado, TipoCanal canal, Pageable pageable) {
        return jpaRepository.buscarPorFiltros(estado, canal, pageable).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Solicitud> buscarConHistorial(CodigoSolicitud codigo) {
        return jpaRepository.buscarConHistorialPorCodigo(codigo.valor()).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Solicitud> buscarPorVariosEstados(List<EstadoSolicitud> estados) {
        return jpaRepository.findByEstadoIn(estados).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarPorEstado(EstadoSolicitud estado) {
        return jpaRepository.countByEstado(estado);
    }

    public Solicitud save(Solicitud solicitud) {
        return guardar(solicitud);
    }

    public List<Solicitud> findByEstado(EstadoSolicitud estado) {
        return buscarPorEstado(estado);
    }
}
