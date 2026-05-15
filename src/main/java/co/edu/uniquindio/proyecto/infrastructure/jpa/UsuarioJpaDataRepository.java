package co.edu.uniquindio.proyecto.infrastructure.jpa;

import co.edu.uniquindio.proyecto.domain.valueobject.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioJpaDataRepository extends JpaRepository<UsuarioEntity, String> {

    /**
     * Spring Security usa este método para validar email/password
     */
    Optional<UsuarioEntity> findByEmail(String email);

    /**
     * Útil para listar usuarios por rol (Admin dashboard)
     */
    List<UsuarioEntity> findByTipo(TipoUsuario tipo);
}
