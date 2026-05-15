package co.edu.uniquindio.proyecto.infrastructure.security.config;

import co.edu.uniquindio.proyecto.domain.valueobject.EstadoUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoUsuario;
import co.edu.uniquindio.proyecto.infrastructure.jpa.UsuarioEntity;
import co.edu.uniquindio.proyecto.infrastructure.jpa.UsuarioJpaDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultUserInitializer implements CommandLineRunner {

    private final DefaultUserProperties props;
    private final UsuarioJpaDataRepository repository;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        normalizarPasswordsExistentes();

        if (props.getUsers() == null || props.getUsers().isEmpty()) {
            return;
        }

        props.getUsers().forEach(user -> repository.findByEmail(user.getEmail()).orElseGet(() -> {
            UsuarioEntity entity = new UsuarioEntity();
            entity.setId(user.getId() == null || user.getId().isBlank()
                    ? "USR-" + Math.abs(user.getEmail().hashCode())
                    : user.getId());
            entity.setNombre(user.getNombre() == null || user.getNombre().isBlank()
                    ? user.getEmail().split("@")[0]
                    : user.getNombre());
            entity.setEmail(user.getEmail());
            entity.setPassword(encoder.encode(user.getPassword()));
            entity.setTipo(TipoUsuario.valueOf(user.getTipo().toUpperCase()));
            entity.setEstado(EstadoUsuario.ACTIVO);
            return repository.save(entity);
        }));
    }

    private void normalizarPasswordsExistentes() {
        repository.findAll().forEach(user -> {
            String password = user.getPassword();
            if (password == null || !password.startsWith("$2")) {
                user.setPassword(encoder.encode(password == null || password.isBlank() ? "NO_AUTH" : password));
                repository.save(user);
            }
        });
    }
}
