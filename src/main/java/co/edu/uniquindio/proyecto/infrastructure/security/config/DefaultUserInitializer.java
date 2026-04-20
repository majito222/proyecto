package co.edu.uniquindio.proyecto.infrastructure.security.config;

import co.edu.uniquindio.proyecto.domain.valueobject.EstadoUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoUsuario;
import co.edu.uniquindio.proyecto.infrastructure.jpa.UsuarioEntity;
import co.edu.uniquindio.proyecto.infrastructure.jpa.UsuarioJpaDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultUserInitializer implements CommandLineRunner {

    @Qualifier("app.default-users-co.edu.uniquindio.proyecto.infrastructure.security.config.DefaultUserProperties")
    private final DefaultUserProperties props;
    private final UsuarioJpaDataRepository repository;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            props.getUsers().forEach(user -> {

                UsuarioEntity entity = new UsuarioEntity();
                entity.setId("USR-" + Math.abs(user.getEmail().hashCode()));
                entity.setNombre(user.getEmail().split("@")[0]);
                entity.setEmail(user.getEmail());
                entity.setPassword(encoder.encode(user.getPassword()));
                entity.setTipo(TipoUsuario.valueOf(user.getTipo().toUpperCase()));
                entity.setEstado(EstadoUsuario.ACTIVO);

                repository.save(entity);

                System.out.println("Usuario creado: " + user.getEmail());
            });

            System.out.println("Seed JWT completado!");
        }
    }
}