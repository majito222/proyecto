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
import co.edu.uniquindio.proyecto.domain.valueobject.CodigoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.DescripcionSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoCanal;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoSolicitud;
import co.edu.uniquindio.proyecto.infrastructure.exception.GlobalExceptionHandler;
import co.edu.uniquindio.proyecto.infrastructure.mapper.SolicitudMapper;
import co.edu.uniquindio.proyecto.infrastructure.mapper.SolicitudRequestMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SolicitudController.class)
@Import(GlobalExceptionHandler.class)
class SolicitudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IniciarAtencionUseCase iniciarAtencionUseCase;

    @MockitoBean
    private MarcarAtendidaUseCase marcarAtendidaUseCase;

    @MockitoBean
    private ClasificarSolicitudUseCase clasificarSolicitudUseCase;

    @MockitoBean
    private PriorizarSolicitudUseCase priorizarSolicitudUseCase;

    @MockitoBean
    private CrearSolicitudUseCase crearSolicitudUseCase;

    @MockitoBean
    private AsignarResponsableUseCase asignarResponsableUseCase;

    @MockitoBean
    private CerrarSolicitudUseCase cerrarSolicitudUseCase;

    @MockitoBean
    private CancelarSolicitudUseCase cancelarSolicitudUseCase;

    @MockitoBean
    private ConsultarSolicitudesUseCase consultarSolicitudesUseCase;

    @MockitoBean
    private ConsultarSolicitudesAvanzadasUseCase consultarSolicitudesAvanzadasUseCase;

    @MockitoBean
    private ConsultarSolicitudPorCodigoUseCase consultarSolicitudPorCodigoUseCase;

    @MockitoBean
    private SolicitudMapper solicitudMapper;

    @MockitoBean
    private SolicitudRequestMapper solicitudRequestMapper;

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
                estudianteId,
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
                                  "estudianteId": "123456",
                                  "canal": "CSU",
                                  "tipo": "CONSULTA_ACADEMICA",
                                  "descripcion": "Necesito apoyo con una solicitud academica del semestre actual."
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/solicitudes/SOL-001"));
    }

    @Test
    void crearSolicitudConPayloadInvalidoDebeRetornarBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "estudianteId": "",
                                  "descripcion": "corta"
                                }
                                """))
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
                .andExpect(jsonPath("$[0].accion").value("Solicitud registrada"))
                .andExpect(jsonPath("$[0].responsable").value("SISTEMA"))
                .andExpect(jsonPath("$[0].fecha").value("2026-04-13T10:00:00"));
    }
}
