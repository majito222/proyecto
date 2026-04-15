package co.edu.uniquindio.proyecto.infrastructure.jpa;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoUsuario;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional
public class UsuarioJpaRepositoryImpl implements UsuarioRepository {

    private final UsuarioJpaDataRepository dataRepository;
    private final UsuarioPersistenceMapper mapper;

    public UsuarioJpaRepositoryImpl(UsuarioJpaDataRepository dataRepository,
                                    UsuarioPersistenceMapper mapper) {
        this.dataRepository = dataRepository;
        this.mapper = mapper;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        return save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario buscarPorId(IdUsuario id) {
        return findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado: " + id.valor()));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(Email email) {
        return dataRepository.findByEmail(email.valor())
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> buscarPorTipo(TipoUsuario tipo) {
        return dataRepository.findByTipo(tipo).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return dataRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void eliminarPorId(IdUsuario id) {
        dataRepository.deleteById(id.valor());
    }

    @Override
    public Optional<Usuario> findById(IdUsuario id) {
        return dataRepository.findById(id.valor())
                .map(mapper::toDomain);
    }

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioEntity entity = mapper.toEntity(usuario);
        UsuarioEntity saved = dataRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
