package co.edu.uniquindio.proyecto.infrastructure.jpa;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
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
        UsuarioEntity entity = mapper.toEntity(usuario);
        UsuarioEntity saved = dataRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario buscarPorId(IdUsuario id) {
        return dataRepository.findById(id.valor())
                .map(mapper::toDomain)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado: " + id.valor()));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(Email email) {
        return dataRepository.findByEmail(email.valor()).map(mapper::toDomain);
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
    public Page<Usuario> listarTodos(Pageable pageable) {
        return dataRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public void eliminarPorId(IdUsuario id) {
        dataRepository.deleteById(id.valor());
    }

    public Usuario save(Usuario usuario) {
        return guardar(usuario);
    }
}
