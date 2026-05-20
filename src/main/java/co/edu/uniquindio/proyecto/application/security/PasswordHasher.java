package co.edu.uniquindio.proyecto.application.security;

public interface PasswordHasher {

    String hash(String rawPassword);
}
