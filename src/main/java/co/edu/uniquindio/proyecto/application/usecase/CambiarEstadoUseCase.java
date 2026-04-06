package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.entity.Solicitud; // O domain.model.Solicitud según tu proyecto

public class CambiarEstadoUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;

    public CambiarEstadoUseCase(SolicitudRepository solicitudRepository, UsuarioRepository usuarioRepository) {
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void execute(String solicitudId, String nuevoEstado) {
        // Aquí iría tu lógica para cambiar el estado
        // Por ahora lo dejamos así para que compile
    }
}