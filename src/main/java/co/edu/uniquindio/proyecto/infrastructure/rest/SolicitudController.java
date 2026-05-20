package co.edu.uniquindio.proyecto.infrastructure.rest;

import co.edu.uniquindio.proyecto.application.AsignarResponsableUseCase;
import co.edu.uniquindio.proyecto.application.CancelarSolicitudUseCase;
import co.edu.uniquindio.proyecto.application.CerrarSolicitudUseCase;
import co.edu.uniquindio.proyecto.application.ClasificarSolicitudUseCase;
import co.edu.uniquindio.proyecto.application.ConsultarSolicitudPorCodigoUseCase;
import co.edu.uniquindio.proyecto.application.ConsultarSolicitudesAvanzadasUseCase;
import co.edu.uniquindio.proyecto.application.ConsultarSolicitudesUseCase;
import co.edu.uniquindio.proyecto.application.CrearSolicitudUseCase;
import co.edu.uniquindio.proyecto.application.IniciarAtencionUseCase;
import co.edu.uniquindio.proyecto.application.MarcarAtendidaUseCase;
import co.edu.uniquindio.proyecto.application.PriorizarSolicitudUseCase;
import co.edu.uniquindio.proyecto.application.dto.request.AsignarResponsableRequest;
import co.edu.uniquindio.proyecto.application.dto.request.CancelarSolicitudRequest;
import co.edu.uniquindio.proyecto.application.dto.request.CerrarSolicitudRequest;
import co.edu.uniquindio.proyecto.application.dto.request.ClasificarSolicitudRequest;
import co.edu.uniquindio.proyecto.application.dto.request.CrearSolicitudRequest;
import co.edu.uniquindio.proyecto.application.dto.request.IniciarAtencionRequest;
import co.edu.uniquindio.proyecto.application.dto.request.MarcarAtendidaRequest;
import co.edu.uniquindio.proyecto.application.dto.request.PriorizarSolicitudRequest;
import co.edu.uniquindio.proyecto.application.dto.response.ErrorResponse;
import co.edu.uniquindio.proyecto.application.dto.response.HistorialResponse;
import co.edu.uniquindio.proyecto.application.dto.response.PaginaResponse;
import co.edu.uniquindio.proyecto.application.dto.response.SolicitudDetalleResponse;
import co.edu.uniquindio.proyecto.application.dto.response.SolicitudResumenResponse;
import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.CodigoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.EstadoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.PrioridadSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoSolicitud;
import co.edu.uniquindio.proyecto.infrastructure.mapper.SolicitudMapper;
import co.edu.uniquindio.proyecto.infrastructure.mapper.SolicitudRequestMapper;
import co.edu.uniquindio.proyecto.infrastructure.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
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
@Tag(name = "Solicitudes", description = "Operaciones REST del agregado Solicitud")
public class SolicitudController {

    private final IniciarAtencionUseCase iniciarAtencionUseCase;
    private final MarcarAtendidaUseCase marcarAtendidaUseCase;
    private final ClasificarSolicitudUseCase clasificarSolicitudUseCase;
    private final PriorizarSolicitudUseCase priorizarSolicitudUseCase;
    private final CrearSolicitudUseCase crearSolicitudUseCase;
    private final AsignarResponsableUseCase asignarResponsableUseCase;
    private final CerrarSolicitudUseCase cerrarSolicitudUseCase;
    private final CancelarSolicitudUseCase cancelarSolicitudUseCase;
    private final ConsultarSolicitudesUseCase consultarSolicitudesUseCase;
    private final ConsultarSolicitudesAvanzadasUseCase consultarSolicitudesAvanzadasUseCase;
    private final ConsultarSolicitudPorCodigoUseCase consultarSolicitudPorCodigoUseCase;
    private final SolicitudMapper solicitudMapper;
    private final SolicitudRequestMapper solicitudRequestMapper;

    @PostMapping
    @Operation(summary = "Crear solicitud")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Solicitud creada"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos invalidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PreAuthorize("hasRole('ESTUDIANTE')")
    public ResponseEntity<SolicitudDetalleResponse> crearSolicitud(
            @Valid @RequestBody CrearSolicitudRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        var datos = solicitudRequestMapper.toDomainData(request);
        var solicitud = crearSolicitudUseCase.ejecutar(
                authenticatedUserId(currentUser),
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
    @PreAuthorize("hasRole('ADMINISTRADOR')")
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
    @PreAuthorize("hasRole('FUNCIONARIO')")
    public ResponseEntity<SolicitudDetalleResponse> clasificarSolicitud(
            @PathVariable String codigo,
            @Valid @RequestBody ClasificarSolicitudRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        var solicitud = clasificarSolicitudUseCase.ejecutar(
                new CodigoSolicitud(codigo),
                authenticatedUserId(currentUser),
                TipoSolicitud.valueOf(request.tipo().name())
        );

        return ResponseEntity.ok(solicitudMapper.toDetalleResponse(solicitud));
    }

    @PostMapping("/{codigo}/prioridad")
    @Operation(summary = "Priorizar solicitud")
    @PreAuthorize("hasRole('FUNCIONARIO')")
    public ResponseEntity<SolicitudDetalleResponse> priorizarSolicitud(
            @PathVariable String codigo,
            @Valid @RequestBody PriorizarSolicitudRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        var solicitud = priorizarSolicitudUseCase.ejecutar(
                new CodigoSolicitud(codigo),
                authenticatedUserId(currentUser),
                new PrioridadSolicitud(
                        PrioridadSolicitud.Nivel.valueOf(request.nivel().name()),
                        request.justificacion()
                )
        );

        return ResponseEntity.ok(solicitudMapper.toDetalleResponse(solicitud));
    }

    @PostMapping("/{codigo}/atencion/inicio")
    @Operation(summary = "Iniciar atencion")
    @PreAuthorize("hasRole('FUNCIONARIO')")
    public ResponseEntity<SolicitudDetalleResponse> iniciarAtencion(
            @PathVariable String codigo,
            @Valid @RequestBody IniciarAtencionRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        var solicitud = iniciarAtencionUseCase.ejecutar(
                new CodigoSolicitud(codigo),
                authenticatedUserId(currentUser)
        );

        return ResponseEntity.ok(solicitudMapper.toDetalleResponse(solicitud));
    }

    @PostMapping("/{codigo}/atencion/finalizacion")
    @Operation(summary = "Finalizar atencion")
    @PreAuthorize("hasRole('FUNCIONARIO')")
    public ResponseEntity<SolicitudDetalleResponse> marcarAtendida(
            @PathVariable String codigo,
            @Valid @RequestBody MarcarAtendidaRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        var solicitud = marcarAtendidaUseCase.ejecutar(
                new CodigoSolicitud(codigo),
                authenticatedUserId(currentUser)
        );

        return ResponseEntity.ok(solicitudMapper.toDetalleResponse(solicitud));
    }

    @PostMapping("/{codigo}/cierre")
    @Operation(summary = "Cerrar solicitud")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<SolicitudDetalleResponse> cerrarSolicitud(
            @PathVariable String codigo,
            @Valid @RequestBody CerrarSolicitudRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        var solicitud = cerrarSolicitudUseCase.ejecutar(
                new CodigoSolicitud(codigo),
                authenticatedUserId(currentUser),
                request.observacion()
        );

        return ResponseEntity.ok(solicitudMapper.toDetalleResponse(solicitud));
    }

    @PostMapping("/{codigo}/cancelacion")
    @Operation(summary = "Cancelar solicitud")
    @PreAuthorize("hasAnyRole('FUNCIONARIO', 'ADMINISTRADOR')")
    public ResponseEntity<SolicitudDetalleResponse> cancelarSolicitud(
            @PathVariable String codigo,
            @Valid @RequestBody CancelarSolicitudRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        var solicitud = cancelarSolicitudUseCase.ejecutar(
                new CodigoSolicitud(codigo),
                authenticatedUserId(currentUser),
                request.observacion()
        );

        return ResponseEntity.ok(solicitudMapper.toDetalleResponse(solicitud));
    }

    @GetMapping
    @Operation(summary = "Consultar solicitudes con filtros y paginacion")
    public ResponseEntity<PaginaResponse<SolicitudResumenResponse>> consultar(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String canal,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamano,
            @RequestParam(defaultValue = "fechaCreacion") String ordenarPor,
            @RequestParam(defaultValue = "desc") String direccion) {

        Sort.Direction sortDirection = "asc".equalsIgnoreCase(direccion)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pagina, tamano, Sort.by(sortDirection, ordenarPor));

        Page<SolicitudResumenResponse> solicitudes = consultarSolicitudesUseCase.ejecutar(estado, canal, pageable)
                .map(solicitudMapper::toResumenResponse);

        return ResponseEntity.ok(PaginaResponse.of(solicitudes));
    }

    @GetMapping("/{codigo}")
    @Operation(summary = "Consultar solicitud por codigo")
    public ResponseEntity<SolicitudDetalleResponse> obtenerPorCodigo(@PathVariable String codigo) {
        var solicitud = consultarSolicitudPorCodigoUseCase.ejecutar(new CodigoSolicitud(codigo));
        return ResponseEntity.ok(solicitudMapper.toDetalleResponse(solicitud));
    }

    @GetMapping("/{codigo}/historial")
    @Operation(summary = "Consultar historial de solicitud con paginacion")
    public ResponseEntity<PaginaResponse<HistorialResponse>> obtenerHistorial(
            @PathVariable String codigo,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamano) {

        var solicitud = consultarSolicitudPorCodigoUseCase.ejecutar(new CodigoSolicitud(codigo));
        Pageable pageable = PageRequest.of(pagina, tamano);
        List<HistorialResponse> historial = solicitudMapper.toHistorialResponseList(solicitud.obtenerHistorial());

        return ResponseEntity.ok(PaginaResponse.of(historial, pageable));
    }

    @GetMapping("/gui11/estado-prioridad/{estado}")
    @Operation(summary = "Solicitudes por estado ordenadas por prioridad con paginacion")
    public ResponseEntity<PaginaResponse<SolicitudResumenResponse>> estadoPrioridad(
            @PathVariable EstadoSolicitud estado,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamano) {

        Pageable pageable = PageRequest.of(pagina, tamano, Sort.by(Sort.Direction.DESC, "prioridad.nivel"));
        Page<SolicitudResumenResponse> solicitudes = consultarSolicitudesAvanzadasUseCase
                .buscarPorEstadoPrioridad(estado, pageable)
                .map(solicitudMapper::toResumenResponse);

        return ResponseEntity.ok(PaginaResponse.of(solicitudes));
    }

    @GetMapping("/gui11/codigo")
    @Operation(summary = "Busqueda por codigo o descripcion con paginacion")
    public ResponseEntity<PaginaResponse<SolicitudResumenResponse>> buscarPorCodigo(
            @RequestParam String codigo,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamano) {

        Pageable pageable = PageRequest.of(pagina, tamano, Sort.by(Sort.Direction.DESC, "fechaCreacion"));
        Page<SolicitudResumenResponse> solicitudes = consultarSolicitudesAvanzadasUseCase.buscarPorTexto(codigo, pageable)
                .map(solicitudMapper::toResumenResponse);

        return ResponseEntity.ok(PaginaResponse.of(solicitudes));
    }

    @GetMapping("/gui11/activas")
    @Operation(summary = "Solicitudes activas paginadas")
    public ResponseEntity<PaginaResponse<SolicitudResumenResponse>> activasPaginadas(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamano) {

        Pageable pageable = PageRequest.of(pagina, tamano, Sort.by("fechaCreacion").descending());
        Page<SolicitudResumenResponse> solicitudes = consultarSolicitudesAvanzadasUseCase.buscarActivasPaginadas(pageable)
                .map(solicitudMapper::toResumenResponse);

        return ResponseEntity.ok(PaginaResponse.of(solicitudes));
    }

    @GetMapping("/gui11/pendientes-alta")
    @Operation(summary = "Pendientes sin asignar de alta prioridad con paginacion")
    public ResponseEntity<PaginaResponse<SolicitudResumenResponse>> pendientesAltaPrioridad(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamano) {

        Pageable pageable = PageRequest.of(pagina, tamano, Sort.by("fechaCreacion").descending());
        Page<SolicitudResumenResponse> solicitudes = consultarSolicitudesAvanzadasUseCase
                .buscarSinAsignarAltaPrioridad(PrioridadSolicitud.Nivel.ALTA, pageable)
                .map(solicitudMapper::toResumenResponse);

        return ResponseEntity.ok(PaginaResponse.of(solicitudes));
    }

    private IdUsuario authenticatedUserId(CustomUserDetails currentUser) {
        if (currentUser != null) {
            return new IdUsuario(currentUser.usuario().getId());
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails principal) {
            return new IdUsuario(principal.usuario().getId());
        }

        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null && requestAttributes.getRequest().getSession(false) != null) {
            Object context = requestAttributes.getRequest()
                    .getSession(false)
                    .getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
            if (context instanceof SecurityContext securityContext
                    && securityContext.getAuthentication() != null
                    && securityContext.getAuthentication().getPrincipal() instanceof CustomUserDetails principal) {
                return new IdUsuario(principal.usuario().getId());
            }
        }

        throw new IllegalStateException("No hay un usuario autenticado disponible");
    }
}
