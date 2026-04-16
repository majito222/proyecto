package co.edu.uniquindio.proyecto.infrastructure.jpa;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
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
        SolicitudEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Solicitud buscarPorCodigo(CodigoSolicitud codigo) {
        return jpaRepository.findByCodigo(codigo.valor())
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public List<Solicitud> buscarPorEstado(EstadoSolicitud estado) {
        SolicitudEntity.EstadoSolicitudEnum enumEstado =
                SolicitudEntity.EstadoSolicitudEnum.valueOf(estado.name());
        return jpaRepository.findByEstado(enumEstado)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Solicitud> buscarPorEstudiante(IdUsuario id) {
        return jpaRepository.findByEstudianteId(id.valor())
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Solicitud> listarTodas() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void eliminarPorCodigo(CodigoSolicitud codigo) {
        jpaRepository.findByCodigo(codigo.valor()).ifPresent(jpaRepository::delete);
    }

    @Override
    public Solicitud save(Solicitud solicitud) {
        return null;
    }

    @Override
    public Optional<Solicitud> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<Solicitud> findByCodigo(CodigoSolicitud codigo) {
        return Optional.empty();
    }

    @Override
    public List<Solicitud> findByEstado(EstadoSolicitud estado) {
        return List.of();
    }


    public List<Solicitud> buscarPorEstadoPrioridad(EstadoSolicitud estado) {
        SolicitudEntity.EstadoSolicitudEnum enumEstado =
                SolicitudEntity.EstadoSolicitudEnum.valueOf(estado.name());
        return jpaRepository.findByEstadoOrderByPrioridad_NivelDesc(enumEstado)
                .stream().map(mapper::toDomain).toList();
    }

    public List<Solicitud> buscarSinAsignarAltaPrioridad(PrioridadSolicitud.Nivel nivel) {
        return jpaRepository.buscarSinAsignarPorPrioridadAlta(nivel)
                .stream().map(mapper::toDomain).toList();
    }

    public List<Solicitud> buscarPorCodigoParcial(String codigo) {
        return jpaRepository.buscarPorCodigo(codigo)
                .stream().map(mapper::toDomain).toList();
    }

    public Page<Solicitud> buscarActivasPaginadas(Pageable pageable) {
        return jpaRepository.buscarActivasPaginadas(pageable)
                .map(mapper::toDomain);
    }

    public Optional<Solicitud> buscarConHistorial(CodigoSolicitud codigo) {
        return jpaRepository.buscarConHistorialPorCodigo(codigo.valor())
                .map(mapper::toDomain);
    }

    public List<Solicitud> buscarPorVariosEstados(List<EstadoSolicitud> estados) {
        List<SolicitudEntity.EstadoSolicitudEnum> enums = estados.stream()
                .map(e -> SolicitudEntity.EstadoSolicitudEnum.valueOf(e.name()))
                .toList();
        return jpaRepository.buscarPorVariosEstados(enums)
                .stream().map(mapper::toDomain).toList();
    }
}