package co.edu.uniquindio.proyecto.application.dto.response;

public record UsuarioDetalleResponse(
        String id,
        String nombre,
        String email,
        String tipo,
        String estado
) {
}
