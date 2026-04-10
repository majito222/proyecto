package co.edu.uniquindio.proyecto.infrastructure.rest;

import co.edu.uniquindio.proyecto.application.dto.response.UsuarioDetalleResponse;
import co.edu.uniquindio.proyecto.application.usecase.ConsultarUsuarioPorIdUseCase;
import co.edu.uniquindio.proyecto.application.usecase.CrearUsuarioUseCase;
import co.edu.uniquindio.proyecto.application.usecase.ListarUsuariosUseCase;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoUsuario;
import co.edu.uniquindio.proyecto.infrastructure.exception.GlobalExceptionHandler;
import co.edu.uniquindio.proyecto.infrastructure.mapper.UsuarioMapper;
import co.edu.uniquindio.proyecto.infrastructure.mapper.UsuarioRequestMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
@Import(GlobalExceptionHandler.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CrearUsuarioUseCase crearUsuarioUseCase;

    @MockitoBean
    private ConsultarUsuarioPorIdUseCase consultarUsuarioPorIdUseCase;

    @MockitoBean
    private ListarUsuariosUseCase listarUsuariosUseCase;

    @MockitoBean
    private UsuarioMapper usuarioMapper;

    @MockitoBean
    private UsuarioRequestMapper usuarioRequestMapper;

    @Test
    void crearUsuarioDebeRetornarCreatedConLocation() throws Exception {
        var id = new IdUsuario("123456");
        var email = new Email("ana@uq.edu.co");
        var usuario = new Usuario(id, "Ana Perez", email, TipoUsuario.ESTUDIANTE);
        var requestData = new UsuarioRequestMapper.UsuarioData(
                "Ana Perez",
                email,
                TipoUsuario.ESTUDIANTE
        );
        var response = new UsuarioDetalleResponse(
                "123456",
                "Ana Perez",
                "ana@uq.edu.co",
                "ESTUDIANTE",
                "ACTIVO"
        );

        when(usuarioRequestMapper.toDomainData(any())).thenReturn(requestData);
        when(crearUsuarioUseCase.ejecutar(eq("Ana Perez"), eq(email), eq(TipoUsuario.ESTUDIANTE)))
                .thenReturn(usuario);
        when(usuarioMapper.toDetalleResponse(usuario)).thenReturn(response);

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Ana Perez",
                                  "email": "ana@uq.edu.co",
                                  "tipo": "ESTUDIANTE"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/usuarios/123456"));
    }

    @Test
    void crearUsuarioConPayloadInvalidoDebeRetornarBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "",
                                  "email": "no-es-email"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}
