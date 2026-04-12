package co.edu.uniquindio.proyecto.infrastructure.jpa;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.CodigoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.EstadoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class SolicitudJpaRepositoryImpl implements SolicitudRepository {

    private final SolicitudJpaRepository jpaRepository;
    private final SolicitudPersistenceMapper mapper;

    public SolicitudJpaRepositoryImpl(SolicitudJpaRepository jpaRepository,
                                      SolicitudPersistenceMapper mapper, UsuarioRepository usuarioRepository) {
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
        Optional<SolicitudEntity> entityOpt = jpaRepository.findByCodigo(codigo.valor());
        return entityOpt.map(mapper::toDomain).orElse(null);
    }

    @Override
    public List<Solicitud> buscarPorEstado(EstadoSolicitud estado) {
        SolicitudEntity.EstadoSolicitudEnum estadoEnum =
                SolicitudEntity.EstadoSolicitudEnum.valueOf(estado.name());
        return jpaRepository.findByEstado(estadoEnum)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Solicitud> buscarPorEstudiante(IdUsuario estudianteId) {
        return jpaRepository.findBySolicitanteId(Long.valueOf(estudianteId.valor()))
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Solicitud> listarTodas() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void eliminarPorCodigo(CodigoSolicitud codigo) {
        jpaRepository.findByCodigo(codigo.valor())
                .ifPresent(jpaRepository::delete);
    }

    // Spring Data overrides
    @Override
    public Solicitud save(Solicitud solicitud) {
        return guardar(solicitud);
    }

    @Override
    public Optional<Solicitud> findById(String id) {
        return Optional.ofNullable(buscarPorCodigo(new CodigoSolicitud(id)));
    }

    @Override
    public Optional<Solicitud> findByCodigo(CodigoSolicitud codigo) {
        return Optional.ofNullable(buscarPorCodigo(codigo));
    }

    @Override
    public List<Solicitud> findByEstado(EstadoSolicitud estado) {
        return buscarPorEstado(estado);
    }
}