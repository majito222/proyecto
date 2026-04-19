package co.edu.uniquindio.proyecto.infrastructure.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "app.default-users")
@Getter
@Setter
public class DefaultUserProperties {

    private List<User> users;

    @Getter
    @Setter
    public static class User {
        private String email;
        private String password;
        private String tipo;
    }
}