package co.edu.uniquindio.proyecto.infrastructure.controller;

import co.edu.uniquindio.proyecto.application.usecase.*;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import co.edu.uniquindio.proyecto.infrastructure.dto.*;
import co.edu.uniquindio.proyecto.infrastructure.mapper.SolicitudMapper;
import co.edu.uniquindio.proyecto.infrastructure.mapper.SolicitudRequestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gestión de solicitudes.
 */
@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SolicitudController {

    private final IniciarAtencionUseCase iniciarAtencionUseCase;
    private final MarcarAtendidaUseCase marcarAtendidaUseCase;
    private final ClasificarSolicitudUseCase clasificarSolicitudUseCase;
    private final PriorizarSolicitudUseCase priorizarSolicitudUseCase;
    private final CrearSolicitudUseCase crearSolicitudUseCase;
    private final AsignarResponsableUseCase asignarResponsableUseCase;
    private final CerrarSolicitudUseCase cerrarSolicitudUseCase;
    private final ConsultarSolicitudesPorEstadoUseCase consultarSolicitudesPorEstadoUseCase;
    private final SolicitudMapper solicitudMapper;
    private final SolicitudRequestMapper solicitudRequestMapper;

    /**
     * Crea una nueva solicitud.
     */
    @PostMapping
    public ResponseEntity<SolicitudResponse> crearSolicitud(
            @Valid @RequestBody CrearSolicitudRequest request) {
        
        var datos = solicitudRequestMapper.toDomainData(request);
        CodigoSolicitud codigo = CodigoSolicitud.generar();
        
        var solicitud = crearSolicitudUseCase.ejecutar(
            datos.estudianteId(),
            codigo,
            datos.canal(),
            datos.tipo(),
            datos.descripcion()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(solicitudMapper.toResponse(solicitud));
    }

    /**
     * Asigna un responsable a una solicitud.
     */
    @PutMapping("/{codigo}/responsable")
    public ResponseEntity<SolicitudResponse> asignarResponsable(
            @PathVariable String codigo,
            @Valid @RequestBody AsignarResponsableRequest request) {
        
        var solicitud = asignarResponsableUseCase.ejecutar(
            new CodigoSolicitud(codigo),
            new IdUsuario(request.funcionarioId())
        );
        
        return ResponseEntity.ok(solicitudMapper.toResponse(solicitud));
    }

    /**
     * Clasifica una solicitud.
     */
    @PostMapping("/{codigo}/clasificar")
    public ResponseEntity<SolicitudResponse> clasificarSolicitud(
            @PathVariable String codigo,
            @Valid @RequestBody ClasificarSolicitudRequest request) {
        
        var solicitud = clasificarSolicitudUseCase.ejecutar(
            new CodigoSolicitud(codigo),
            new IdUsuario(request.funcionarioId()),
            convertirTipoSolicitud(request.tipo())
        );
        
        return ResponseEntity.ok(solicitudMapper.toResponse(solicitud));
    }

    /**
     * Prioriza una solicitud.
     */
    @PostMapping("/{codigo}/priorizar")
    public ResponseEntity<SolicitudResponse> priorizarSolicitud(
            @PathVariable String codigo,
            @Valid @RequestBody PriorizarSolicitudRequest request) {
        
        var solicitud = priorizarSolicitudUseCase.ejecutar(
            new CodigoSolicitud(codigo),
            new IdUsuario(request.funcionarioId()),
            new PrioridadSolicitud(
                convertirNivelPrioridad(request.nivel()),
                request.justificacion()
            )
        );
        
        return ResponseEntity.ok(solicitudMapper.toResponse(solicitud));
    }

    /**
     * Inicia atención de una solicitud.
     */
    @PostMapping("/{codigo}/iniciar-atencion")
    public ResponseEntity<SolicitudResponse> iniciarAtencion(
            @PathVariable String codigo,
            @Valid @RequestBody AsignarResponsableRequest request) {
        
        var solicitud = iniciarAtencionUseCase.ejecutar(
            new CodigoSolicitud(codigo),
            new IdUsuario(request.funcionarioId())
        );
        
        return ResponseEntity.ok(solicitudMapper.toResponse(solicitud));
    }

    /**
     * Marca solicitud como atendida.
     */
    @PostMapping("/{codigo}/marcar-atendida")
    public ResponseEntity<SolicitudResponse> marcarAtendida(
            @PathVariable String codigo,
            @Valid @RequestBody AsignarResponsableRequest request) {
        
        var solicitud = marcarAtendidaUseCase.ejecutar(
            new CodigoSolicitud(codigo),
            new IdUsuario(request.funcionarioId())
        );
        
        return ResponseEntity.ok(solicitudMapper.toResponse(solicitud));
    }

    /**
     * Cierra una solicitud.
     */
    @PostMapping("/{codigo}/cerrar")
    public ResponseEntity<SolicitudResponse> cerrarSolicitud(
            @PathVariable String codigo,
            @Valid @RequestBody CerrarSolicitudRequest request) {
        
        var solicitud = cerrarSolicitudUseCase.ejecutar(
            new CodigoSolicitud(codigo),
            new IdUsuario(request.administradorId()),
            request.observacion()
        );
        
        return ResponseEntity.ok(solicitudMapper.toResponse(solicitud));
    }

    /**
     * Consulta solicitudes por estado.
     */
    @GetMapping
    public ResponseEntity<List<SolicitudResponse>> consultarPorEstado(
            @RequestParam(required = false) String estado) {
        
        List<Solicitud> solicitudes;
        if (estado != null && !estado.isBlank()) {
            EstadoSolicitud estadoEnum = convertirEstado(estado);
            solicitudes = consultarSolicitudesPorEstadoUseCase.ejecutar(estadoEnum);
        } else {
            // Si no se especifica estado, se podrían devolver todas
            solicitudes = List.of(); // Implementar caso de uso si es necesario
        }
        
        return ResponseEntity.ok(solicitudMapper.toResponseList(solicitudes));
    }

    /**
     * Obtiene una solicitud por su código.
     */
    @GetMapping("/{codigo}")
    public ResponseEntity<SolicitudResponse> obtenerPorCodigo(
            @PathVariable String codigo) {
        // Implementar caso de uso si es necesario
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Métodos privados de conversión segura
    private TipoSolicitud convertirTipoSolicitud(ClasificarSolicitudRequest.TipoSolicitudDto tipo) {
        try {
            return TipoSolicitud.valueOf(tipo.name());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de solicitud no válido: " + tipo);
        }
    }

    private PrioridadSolicitud.Nivel convertirNivelPrioridad(PriorizarSolicitudRequest.NivelPrioridad nivel) {
        try {
            return PrioridadSolicitud.Nivel.valueOf(nivel.name());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Nivel de prioridad no válido: " + nivel);
        }
    }

    private EstadoSolicitud convertirEstado(String estado) {
        try {
            return EstadoSolicitud.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado no válido: " + estado);
        }
    }
}
