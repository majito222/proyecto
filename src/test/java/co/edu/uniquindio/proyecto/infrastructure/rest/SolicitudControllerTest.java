package co.edu.uniquindio.proyecto.infrastructure.rest;

import co.edu.uniquindio.proyecto.application.dto.response.SolicitudDetalleResponse;
import co.edu.uniquindio.proyecto.application.dto.response.HistorialResponse;
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
import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.exception.TransicionEstadoInvalidaException;
import co.edu.uniquindio.proyecto.domain.valueobject.CodigoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.DescripcionSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoCanal;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoSolicitud;
import co.edu.uniquindio.proyecto.infrastructure.exception.GlobalExceptionHandler;
import co.edu.uniquindio.proyecto.infrastructure.mapper.SolicitudMapper;
import co.edu.uniquindio.proyecto.infrastructure.mapper.SolicitudRequestMapper;
import co.edu.uniquindio.proyecto.infrastructure.security.CustomUserDetails;
import co.edu.uniquindio.proyecto.infrastructure.security.jwt.JwtAuthenticationFilter;
import co.edu.uniquindio.proyecto.infrastructure.jpa.UsuarioEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@WebMvcTest(SolicitudController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class SolicitudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IniciarAtencionUseCase iniciarAtencionUseCase;

    @MockBean
    private MarcarAtendidaUseCase marcarAtendidaUseCase;

    @MockBean
    private ClasificarSolicitudUseCase clasificarSolicitudUseCase;

    @MockBean
    private PriorizarSolicitudUseCase priorizarSolicitudUseCase;

    @MockBean
    private CrearSolicitudUseCase crearSolicitudUseCase;

    @MockBean
    private AsignarResponsableUseCase asignarResponsableUseCase;

    @MockBean
    private CerrarSolicitudUseCase cerrarSolicitudUseCase;

    @MockBean
    private CancelarSolicitudUseCase cancelarSolicitudUseCase;

    @MockBean
    private ConsultarSolicitudesUseCase consultarSolicitudesUseCase;

    @MockBean
    private ConsultarSolicitudesAvanzadasUseCase consultarSolicitudesAvanzadasUseCase;

    @MockBean
    private ConsultarSolicitudPorCodigoUseCase consultarSolicitudPorCodigoUseCase;

    @MockBean
    private SolicitudMapper solicitudMapper;

    @MockBean
    private SolicitudRequestMapper solicitudRequestMapper;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void crearSolicitudDebeRetornarCreatedConLocation() throws Exception {
        var codigo = new CodigoSolicitud("SOL-001");
        var estudianteId = new IdUsuario("123456");
        var descripcion = new DescripcionSolicitud(
                "Necesito apoyo con una solicitud academica del semestre actual."
        );
        var solicitud = Solicitud.crear(
                codigo,
                estudianteId,
                "Ana Perez",
                TipoCanal.CSU,
                TipoSolicitud.CONSULTA_ACADEMICA,
                descripcion
        );
        var requestData = new SolicitudRequestMapper.SolicitudData(
                TipoCanal.CSU,
                TipoSolicitud.CONSULTA_ACADEMICA,
                descripcion
        );
        var response = new SolicitudDetalleResponse(
                "SOL-001",
                "123456",
                "Ana Perez",
                "CSU",
                "CONSULTA_ACADEMICA",
                "Necesito apoyo con una solicitud academica del semestre actual.",
                "REGISTRADA",
                null,
                List.of()
        );

        when(solicitudRequestMapper.toDomainData(any())).thenReturn(requestData);
        when(crearSolicitudUseCase.ejecutar(
                eq(estudianteId),
                eq(TipoCanal.CSU),
                eq(TipoSolicitud.CONSULTA_ACADEMICA),
                eq(descripcion)
        )).thenReturn(solicitud);
        when(solicitudMapper.toDetalleResponse(solicitud)).thenReturn(response);

        mockMvc.perform(post("/api/v1/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "canal": "CSU",
                                  "tipo": "CONSULTA_ACADEMICA",
                                  "descripcion": "Necesito apoyo con una solicitud academica del semestre actual."
                                }
                                """)
                        .with(authentication(authenticationFor("123456", "ana@uq.edu.co", "ESTUDIANTE"))))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/solicitudes/SOL-001"));
    }

    @Test
    void crearSolicitudConPayloadInvalidoDebeRetornarBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "descripcion": "corta"
                                }
                                """)
                        .with(authentication(authenticationFor("123456", "ana@uq.edu.co", "ESTUDIANTE"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.ruta").value("/api/v1/solicitudes"))
                .andExpect(jsonPath("$.detalles").isArray());
    }

    @Test
    void consultarHistorialDebeRetornarEventos() throws Exception {
        var solicitud = Solicitud.crear(
                new CodigoSolicitud("SOL-001"),
                new IdUsuario("123456"),
                "Ana Perez",
                TipoCanal.CSU,
                TipoSolicitud.CONSULTA_ACADEMICA,
                new DescripcionSolicitud("Necesito apoyo con una solicitud academica del semestre actual.")
        );
        var historial = List.of(
                new HistorialResponse(
                        "Solicitud registrada",
                        "SISTEMA",
                        "",
                        LocalDateTime.of(2026, 4, 13, 10, 0)
                )
        );

        when(consultarSolicitudPorCodigoUseCase.ejecutar(eq(new CodigoSolicitud("SOL-001"))))
                .thenReturn(solicitud);
        when(solicitudMapper.toHistorialResponseList(solicitud.obtenerHistorial()))
                .thenReturn(historial);

        mockMvc.perform(get("/api/v1/solicitudes/SOL-001/historial"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenido[0].accion").value("Solicitud registrada"))
                .andExpect(jsonPath("$.contenido[0].responsable").value("SISTEMA"))
                .andExpect(jsonPath("$.contenido[0].fecha").value("2026-04-13T10:00:00"))
                .andExpect(jsonPath("$.pagina").value(0))
                .andExpect(jsonPath("$.tamano").value(10));
    }

    @Test
    void consultarSolicitudesDebeRetornarPagina() throws Exception {
        var solicitud = Solicitud.crear(
                new CodigoSolicitud("SOL-001"),
                new IdUsuario("123456"),
                "Ana Perez",
                TipoCanal.CSU,
                TipoSolicitud.CONSULTA_ACADEMICA,
                new DescripcionSolicitud("Necesito apoyo con una solicitud academica del semestre actual.")
        );
        var response = new co.edu.uniquindio.proyecto.application.dto.response.SolicitudResumenResponse(
                "SOL-001",
                "123456",
                "Ana Perez",
                "CONSULTA_ACADEMICA",
                "REGISTRADA",
                null
        );

        when(consultarSolicitudesUseCase.ejecutar(eq("REGISTRADA"), eq("CSU"), any()))
                .thenReturn(new PageImpl<>(List.of(solicitud), PageRequest.of(0, 10), 1));
        when(solicitudMapper.toResumenResponse(solicitud)).thenReturn(response);

        mockMvc.perform(get("/api/v1/solicitudes")
                        .param("estado", "REGISTRADA")
                        .param("canal", "CSU"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenido[0].codigo").value("SOL-001"))
                .andExpect(jsonPath("$.totalElementos").value(1))
                .andExpect(jsonPath("$.primera").value(true));
    }

    @Test
    void obtenerSolicitudNoEncontradaDebeRetornarNotFound() throws Exception {
        when(consultarSolicitudPorCodigoUseCase.ejecutar(eq(new CodigoSolicitud("SOL-404"))))
                .thenThrow(new java.util.NoSuchElementException("Solicitud no encontrada: SOL-404"));

        mockMvc.perform(get("/api/v1/solicitudes/SOL-404"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value("NO_ENCONTRADO"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void cerrarSolicitudConTransicionInvalidaDebeRetornarConflict() throws Exception {
        when(cerrarSolicitudUseCase.ejecutar(
                eq(new CodigoSolicitud("SOL-001")),
                eq(new IdUsuario("900001")),
                eq("La solicitud aun no se encuentra atendida.")
        )).thenThrow(new TransicionEstadoInvalidaException("Solo solicitudes atendidas pueden cerrarse"));

        mockMvc.perform(post("/api/v1/solicitudes/SOL-001/cierre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "observacion": "La solicitud aun no se encuentra atendida."
                                }
                                """)
                        .with(authentication(authenticationFor("900001", "security.admin@uq.edu.co", "ADMINISTRADOR"))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigo").value("TRANSICION_INVALIDA"))
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void consultarSolicitudesConArgumentoInvalidoDebeRetornarBadRequest() throws Exception {
        when(consultarSolicitudesUseCase.ejecutar(eq("DESCONOCIDO"), eq("CSU"), any()))
                .thenThrow(new IllegalArgumentException("Estado invalido: DESCONOCIDO"));

        mockMvc.perform(get("/api/v1/solicitudes")
                        .param("estado", "DESCONOCIDO")
                        .param("canal", "CSU"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("ARGUMENTO_INVALIDO"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void obtenerSolicitudConErrorInesperadoDebeRetornarInternalServerError() throws Exception {
        when(consultarSolicitudPorCodigoUseCase.ejecutar(eq(new CodigoSolicitud("SOL-500"))))
                .thenThrow(new RuntimeException("Fallo inesperado"));

        mockMvc.perform(get("/api/v1/solicitudes/SOL-500"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.codigo").value("INTERNAL_ERROR"))
                .andExpect(jsonPath("$.status").value(500));
    }

    private UsernamePasswordAuthenticationToken authenticationFor(String id, String email, String role) {
        var usuario = new UsuarioEntity();
        usuario.setId(id);
        usuario.setEmail(email);
        usuario.setPassword("$2a$10$abcdefghijklmnopqrstuv");
        usuario.setTipo(co.edu.uniquindio.proyecto.domain.valueobject.TipoUsuario.valueOf(role));
        usuario.setEstado(co.edu.uniquindio.proyecto.domain.valueobject.EstadoUsuario.ACTIVO);
        var principal = new CustomUserDetails(usuario);
        return new UsernamePasswordAuthenticationToken(
                principal,
                null,
                java.util.List.of(new SimpleGrantedAuthority("ROLE_" + role))
        );
    }
}
