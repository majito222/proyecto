package co.edu.uniquindio.proyecto.infrastructure.config;

import co.edu.uniquindio.proyecto.application.*;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.service.UsuarioService;
import co.edu.uniquindio.proyecto.infrastructure.jpa.SolicitudJpaRepository;  // ← NUEVO
import co.edu.uniquindio.proyecto.infrastructure.jpa.SolicitudJpaRepositoryImpl;
import co.edu.uniquindio.proyecto.infrastructure.jpa.SolicitudPersistenceMapper; // ← NUEVO
import co.edu.uniquindio.proyecto.infrastructure.repository.UsuarioRepositoryEnMemoria; // Temporal
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

/**
 * Configuración de beans para inyección de dependencias.
 */
@Configuration
public class ApplicationConfig {

    @Bean
    @Primary
    public SolicitudRepository solicitudRepository(
            @Lazy SolicitudJpaRepository jpaDataRepository,        // Spring Data JPA
            SolicitudPersistenceMapper mapper,               // MapStruct
            UsuarioRepository usuarioRepository              // Para relaciones
    ) {
        return new SolicitudJpaRepositoryImpl(jpaDataRepository, mapper, usuarioRepository);
    }

    @Bean
    public UsuarioRepository usuarioRepository() {
        return new UsuarioRepositoryEnMemoria();
    }

    /**
     * Bean para el servicio de usuarios.
     */
    @Bean
    public UsuarioService usuarioService() {
        return new UsuarioService();
    }

    /**
     * Bean para el caso de uso crear solicitud.
     */
    @Bean
    public CrearSolicitudUseCase crearSolicitudUseCase(
            SolicitudRepository solicitudRepository,
            UsuarioRepository usuarioRepository) {
        return new CrearSolicitudUseCase(solicitudRepository, usuarioRepository);
    }

    /**
     * Bean para el caso de uso asignar responsable.
     */
    @Bean
    public AsignarResponsableUseCase asignarResponsableUseCase(
            SolicitudRepository solicitudRepository,
            UsuarioRepository usuarioRepository) {
        return new AsignarResponsableUseCase(solicitudRepository, usuarioRepository);
    }

    /**
     * Bean para el caso de uso cambiar estado.
     */
    @Bean
    public CambiarEstadoUseCase cambiarEstadoUseCase(
            SolicitudRepository solicitudRepository,
            UsuarioRepository usuarioRepository) {
        return new CambiarEstadoUseCase(solicitudRepository, usuarioRepository);
    }

    /**
     * Bean para el caso de uso cerrar solicitud.
     */
    @Bean
    public CerrarSolicitudUseCase cerrarSolicitudUseCase(
            SolicitudRepository solicitudRepository,
            UsuarioRepository usuarioRepository) {
        return new CerrarSolicitudUseCase(solicitudRepository, usuarioRepository);
    }

    /**
     * Bean para el caso de uso crear usuario.
     */
    @Bean
    public CrearUsuarioUseCase crearUsuarioUseCase(UsuarioRepository usuarioRepository) {
        return new CrearUsuarioUseCase(usuarioRepository);
    }

    /**
     * Bean para el caso de uso iniciar atención.
     */
    @Bean
    public IniciarAtencionUseCase iniciarAtencionUseCase(
            SolicitudRepository solicitudRepository,
            UsuarioRepository usuarioRepository) {
        return new IniciarAtencionUseCase(solicitudRepository, usuarioRepository);
    }

    /**
     * Bean para el caso de uso marcar atendida.
     */
    @Bean
    public MarcarAtendidaUseCase marcarAtendidaUseCase(
            SolicitudRepository solicitudRepository,
            UsuarioRepository usuarioRepository) {
        return new MarcarAtendidaUseCase(solicitudRepository, usuarioRepository);
    }

    /**
     * Bean para el caso de uso clasificar solicitud.
     */
    @Bean
    public ClasificarSolicitudUseCase clasificarSolicitudUseCase(
            SolicitudRepository solicitudRepository,
            UsuarioRepository usuarioRepository) {
        return new ClasificarSolicitudUseCase(solicitudRepository, usuarioRepository);
    }

    /**
     * Bean para el caso de uso priorizar solicitud.
     */
    @Bean
    public PriorizarSolicitudUseCase priorizarSolicitudUseCase(
            SolicitudRepository solicitudRepository,
            UsuarioRepository usuarioRepository) {
        return new PriorizarSolicitudUseCase(solicitudRepository, usuarioRepository);
    }

    /**
     * Bean para el caso de uso consultar solicitudes por estado.
     */
    @Bean
    public ConsultarSolicitudesPorEstadoUseCase consultarSolicitudesPorEstadoUseCase(
            SolicitudRepository solicitudRepository) {
        return new ConsultarSolicitudesPorEstadoUseCase(solicitudRepository);
    }

    @Bean
    public ListarSolicitudesUseCase listarSolicitudesUseCase(
            SolicitudRepository solicitudRepository) {
        return new ListarSolicitudesUseCase(solicitudRepository);
    }

    @Bean
    public ConsultarSolicitudPorCodigoUseCase consultarSolicitudPorCodigoUseCase(
            SolicitudRepository solicitudRepository) {
        return new ConsultarSolicitudPorCodigoUseCase(solicitudRepository);
    }

    @Bean
    public ConsultarSolicitudesUseCase consultarSolicitudesUseCase(
            ListarSolicitudesUseCase listarSolicitudesUseCase,
            ConsultarSolicitudesPorEstadoUseCase consultarSolicitudesPorEstadoUseCase) {
        return new ConsultarSolicitudesUseCase(
                listarSolicitudesUseCase,
                consultarSolicitudesPorEstadoUseCase
        );
    }

    @Bean
    public ConsultarUsuarioPorIdUseCase consultarUsuarioPorIdUseCase(
            UsuarioRepository usuarioRepository) {
        return new ConsultarUsuarioPorIdUseCase(usuarioRepository);
    }

    @Bean
    public ListarUsuariosUseCase listarUsuariosUseCase(
            UsuarioRepository usuarioRepository) {
        return new ListarUsuariosUseCase(usuarioRepository);
    }
}