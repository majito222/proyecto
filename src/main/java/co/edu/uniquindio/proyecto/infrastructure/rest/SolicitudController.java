package co.edu.uniquindio.proyecto.infrastructure.rest;

import co.edu.uniquindio.proyecto.application.dto.request.AsignarResponsableRequest;
import co.edu.uniquindio.proyecto.application.dto.request.CerrarSolicitudRequest;
import co.edu.uniquindio.proyecto.application.dto.request.ClasificarSolicitudRequest;
import co.edu.uniquindio.proyecto.application.dto.request.CrearSolicitudRequest;
import co.edu.uniquindio.proyecto.application.dto.request.IniciarAtencionRequest;
import co.edu.uniquindio.proyecto.application.dto.request.MarcarAtendidaRequest;
import co.edu.uniquindio.proyecto.application.dto.request.PriorizarSolicitudRequest;
import co.edu.uniquindio.proyecto.application.dto.response.ErrorResponse;
import co.edu.uniquindio.proyecto.application.dto.response.SolicitudDetalleResponse;
import co.edu.uniquindio.proyecto.application.dto.response.SolicitudResumenResponse;
import co.edu.uniquindio.proyecto.application.usecase.AsignarResponsableUseCase;
import co.edu.uniquindio.proyecto.application.usecase.CerrarSolicitudUseCase;
import co.edu.uniquindio.proyecto.application.usecase.ClasificarSolicitudUseCase;
import co.edu.uniquindio.proyecto.application.usecase.ConsultarSolicitudPorCodigoUseCase;
import co.edu.uniquindio.proyecto.application.usecase.ConsultarSolicitudesUseCase;
import co.edu.uniquindio.proyecto.application.usecase.CrearSolicitudUseCase;
import co.edu.uniquindio.proyecto.application.usecase.IniciarAtencionUseCase;
import co.edu.uniquindio.proyecto.application.usecase.MarcarAtendidaUseCase;
import co.edu.uniquindio.proyecto.application.usecase.PriorizarSolicitudUseCase;
import co.edu.uniquindio.proyecto.domain.valueobject.CodigoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.PrioridadSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoSolicitud;
import co.edu.uniquindio.proyecto.infrastructure.mapper.SolicitudMapper;
import co.edu.uniquindio.proyecto.infrastructure.mapper.SolicitudRequestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/solicitudes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Solicitudes", description = "Operaciones REST del agregado Solicitud")
public class SolicitudController {

    private final IniciarAtencionUseCase iniciarAtencionUseCase;
    private final MarcarAtendidaUseCase marcarAtendidaUseCase;
    private final ClasificarSolicitudUseCase clasificarSolicitudUseCase;
    private final PriorizarSolicitudUseCase priorizarSolicitudUseCase;
    private final CrearSolicitudUseCase crearSolicitudUseCase;
    private final AsignarResponsableUseCase asignarResponsableUseCase;
    private final CerrarSolicitudUseCase cerrarSolicitudUseCase;
    private final ConsultarSolicitudesUseCase consultarSolicitudesUseCase;
    private final ConsultarSolicitudPorCodigoUseCase consultarSolicitudPorCodigoUseCase;
    private final SolicitudMapper solicitudMapper;
    private final SolicitudRequestMapper solicitudRequestMapper;

    @PostMapping
    @Operation(summary = "Crear solicitud")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Solicitud creada"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<SolicitudDetalleResponse> crearSolicitud(
            @Valid @RequestBody CrearSolicitudRequest request) {

        var datos = solicitudRequestMapper.toDomainData(request);
        var solicitud = crearSolicitudUseCase.ejecutar(
                datos.estudianteId(),
                datos.canal(),
                datos.tipo(),
                datos.descripcion()
        );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{codigo}")
                .buildAndExpand(solicitud.getCodigo().valor())
                .toUri();

        return ResponseEntity.created(location)
                .body(solicitudMapper.toDetalleResponse(solicitud));
    }

    @PutMapping("/{codigo}/responsable")
    @Operation(summary = "Asignar responsable")
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
    @Operation(summary = "Clasificar solicitud")
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
    @Operation(summary = "Priorizar solicitud")
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
    @Operation(summary = "Iniciar atencion")
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
    @Operation(summary = "Finalizar atencion")
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
    @Operation(summary = "Cerrar solicitud")
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
    @Operation(summary = "Consultar solicitudes")
    public ResponseEntity<List<SolicitudResumenResponse>> consultar(
            @RequestParam(required = false) String estado) {
        return ResponseEntity.ok(
                solicitudMapper.toResumenResponseList(consultarSolicitudesUseCase.ejecutar(estado))
        );
    }

    @GetMapping("/{codigo}")
    @Operation(summary = "Consultar solicitud por codigo")
    public ResponseEntity<SolicitudDetalleResponse> obtenerPorCodigo(
            @PathVariable String codigo) {

        var solicitud =
                consultarSolicitudPorCodigoUseCase.ejecutar(new CodigoSolicitud(codigo));

        return ResponseEntity.ok(solicitudMapper.toDetalleResponse(solicitud));
    }
}
