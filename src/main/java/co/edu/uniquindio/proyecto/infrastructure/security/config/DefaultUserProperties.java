package co.edu.uniquindio.proyecto.infrastructure.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.default-users")
@Getter
@Setter
public class DefaultUserProperties {

    private List<User> users;

    @Getter
    @Setter
    public static class User {
        private String id;
        private String nombre;
        private String email;
        private String password;
        private String tipo;
    }
}
