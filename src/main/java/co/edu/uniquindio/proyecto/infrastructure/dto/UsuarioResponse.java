package co.edu.uniquindio.proyecto.infrastructure.dto;

/**
 * DTO para respuesta de usuario.
 */
public record UsuarioResponse(
    
    String id,
    String nombre,
    String email,
    String tipo,
    String estado
) {}
