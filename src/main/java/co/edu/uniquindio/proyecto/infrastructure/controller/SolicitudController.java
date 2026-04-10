package co.edu.uniquindio.proyecto.infrastructure.controller;

import co.edu.uniquindio.proyecto.application.dto.request.AsignarResponsableRequest;
import co.edu.uniquindio.proyecto.application.dto.request.CerrarSolicitudRequest;
import co.edu.uniquindio.proyecto.application.dto.request.ClasificarSolicitudRequest;
import co.edu.uniquindio.proyecto.application.dto.request.CrearSolicitudRequest;
import co.edu.uniquindio.proyecto.application.dto.request.IniciarAtencionRequest;
import co.edu.uniquindio.proyecto.application.dto.request.MarcarAtendidaRequest;
import co.edu.uniquindio.proyecto.application.dto.request.PriorizarSolicitudRequest;
import co.edu.uniquindio.proyecto.application.dto.response.SolicitudDetalleResponse;
import co.edu.uniquindio.proyecto.application.dto.response.SolicitudResumenResponse;
import co.edu.uniquindio.proyecto.application.usecase.*;
import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
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
@RequestMapping("/api/v1/solicitudes")
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
    private final ConsultarSolicitudPorCodigoUseCase consultarSolicitudPorCodigoUseCase;
    private final ListarSolicitudesUseCase listarSolicitudesUseCase;
    private final SolicitudMapper solicitudMapper;
    private final SolicitudRequestMapper solicitudRequestMapper;

    @PostMapping
    public ResponseEntity<SolicitudDetalleResponse> crearSolicitud(
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
                .body(solicitudMapper.toDetalleResponse(solicitud));
    }

    @PutMapping("/{codigo}/responsable")
    public ResponseEntity<SolicitudDetalleResponse> asignarResponsable(
            @PathVariable String codigo,
            @Valid @RequestBody AsignarResponsableRequest request) {

        var solicitud = asignarResponsableUseCase.ejecutar(
                new CodigoSolicitud(codigo),
                new IdUsuario(request.funcionarioId())
        );

        return ResponseEntity.ok(solicitudMapper.toDetalleResponse(solicitud));
    }

    @PostMapping("/{codigo}/clasificacion")
    public ResponseEntity<SolicitudDetalleResponse> clasificarSolicitud(
            @PathVariable String codigo,
            @Valid @RequestBody ClasificarSolicitudRequest request) {

        var solicitud = clasificarSolicitudUseCase.ejecutar(
                new CodigoSolicitud(codigo),
                new IdUsuario(request.funcionarioId()),
                TipoSolicitud.valueOf(request.tipo().name())
        );

        return ResponseEntity.ok(solicitudMapper.toDetalleResponse(solicitud));
    }

    @PostMapping("/{codigo}/prioridad")
    public ResponseEntity<SolicitudDetalleResponse> priorizarSolicitud(
            @PathVariable String codigo,
            @Valid @RequestBody PriorizarSolicitudRequest request) {

        var solicitud = priorizarSolicitudUseCase.ejecutar(
                new CodigoSolicitud(codigo),
                new IdUsuario(request.funcionarioId()),
                new PrioridadSolicitud(
                        PrioridadSolicitud.Nivel.valueOf(request.nivel().name()),
                        request.justificacion()
                )
        );

        return ResponseEntity.ok(solicitudMapper.toDetalleResponse(solicitud));
    }

    @PostMapping("/{codigo}/atencion/inicio")
    public ResponseEntity<SolicitudDetalleResponse> iniciarAtencion(
            @PathVariable String codigo,
            @Valid @RequestBody IniciarAtencionRequest request) {

        var solicitud = iniciarAtencionUseCase.ejecutar(
                new CodigoSolicitud(codigo),
                new IdUsuario(request.funcionarioId())
        );

        return ResponseEntity.ok(solicitudMapper.toDetalleResponse(solicitud));
    }

    @PostMapping("/{codigo}/atencion/finalizacion")
    public ResponseEntity<SolicitudDetalleResponse> marcarAtendida(
            @PathVariable String codigo,
            @Valid @RequestBody MarcarAtendidaRequest request) {

        var solicitud = marcarAtendidaUseCase.ejecutar(
                new CodigoSolicitud(codigo),
                new IdUsuario(request.funcionarioId())
        );

        return ResponseEntity.ok(solicitudMapper.toDetalleResponse(solicitud));
    }

    @PostMapping("/{codigo}/cierre")
    public ResponseEntity<SolicitudDetalleResponse> cerrarSolicitud(
            @PathVariable String codigo,
            @Valid @RequestBody CerrarSolicitudRequest request) {

        var solicitud = cerrarSolicitudUseCase.ejecutar(
                new CodigoSolicitud(codigo),
                new IdUsuario(request.administradorId()),
                request.observacion()
        );

        return ResponseEntity.ok(solicitudMapper.toDetalleResponse(solicitud));
    }

    @GetMapping
    public ResponseEntity<List<SolicitudResumenResponse>> consultarPorEstado(
            @RequestParam(required = false) String estado) {

        if (estado == null || estado.isBlank()) {
            return ResponseEntity.ok(
                    solicitudMapper.toResumenResponseList(listarSolicitudesUseCase.ejecutar())
            );
        }

        EstadoSolicitud estadoEnum = EstadoSolicitud.valueOf(estado.toUpperCase());

        List<Solicitud> solicitudes =
                consultarSolicitudesPorEstadoUseCase.ejecutar(estadoEnum);

        return ResponseEntity.ok(solicitudMapper.toResumenResponseList(solicitudes));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<SolicitudDetalleResponse> obtenerPorCodigo(
            @PathVariable String codigo) {

        Solicitud solicitud =
                consultarSolicitudPorCodigoUseCase.ejecutar(new CodigoSolicitud(codigo));

        return ResponseEntity.ok(solicitudMapper.toDetalleResponse(solicitud));
    }
}
