package co.edu.uniquindio.proyecto.infrastructure.rest;

import co.edu.uniquindio.proyecto.application.dto.response.UsuarioDetalleResponse;
import co.edu.uniquindio.proyecto.application.dto.response.UsuarioResumenResponse;
import co.edu.uniquindio.proyecto.application.ConsultarUsuarioPorIdUseCase;
import co.edu.uniquindio.proyecto.application.CrearUsuarioUseCase;
import co.edu.uniquindio.proyecto.application.ListarUsuariosUseCase;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoUsuario;
import co.edu.uniquindio.proyecto.infrastructure.exception.GlobalExceptionHandler;
import co.edu.uniquindio.proyecto.infrastructure.jpa.UsuarioEntity;
import co.edu.uniquindio.proyecto.infrastructure.mapper.UsuarioMapper;
import co.edu.uniquindio.proyecto.infrastructure.mapper.UsuarioRequestMapper;
import co.edu.uniquindio.proyecto.infrastructure.security.CustomUserDetails;
import co.edu.uniquindio.proyecto.infrastructure.security.jwt.JwtAuthenticationFilter;
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

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CrearUsuarioUseCase crearUsuarioUseCase;

    @MockBean
    private ConsultarUsuarioPorIdUseCase consultarUsuarioPorIdUseCase;

    @MockBean
    private ListarUsuariosUseCase listarUsuariosUseCase;

    @MockBean
    private UsuarioMapper usuarioMapper;

    @MockBean
    private UsuarioRequestMapper usuarioRequestMapper;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void crearUsuarioDebeRetornarCreatedConLocation() throws Exception {
        var id = new IdUsuario("123456");
        var email = new Email("ana@uq.edu.co");
        var usuario = new Usuario(id, "Ana Perez", email, TipoUsuario.ESTUDIANTE);
        var requestData = new UsuarioRequestMapper.UsuarioData(
                "Ana Perez",
                email,
                "Admin123*",
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
        when(crearUsuarioUseCase.ejecutar(eq("Ana Perez"), eq(email), eq("Admin123*"), eq(TipoUsuario.ESTUDIANTE)))
                .thenReturn(usuario);
        when(usuarioMapper.toDetalleResponse(usuario)).thenReturn(response);

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Ana Perez",
                                  "email": "ana@uq.edu.co",
                                  "password": "Admin123*",
                                  "tipo": "ESTUDIANTE"
                                }
                                """)
                        .with(authentication(authenticationFor("900001", "security.admin@uq.edu.co", "ADMINISTRADOR"))))
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
                                  "email": "no-es-email",
                                  "password": "123"
                                }
                                """)
                        .with(authentication(authenticationFor("900001", "security.admin@uq.edu.co", "ADMINISTRADOR"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listarUsuariosDebeRetornarPagina() throws Exception {
        var usuario = new Usuario(
                new IdUsuario("123456"),
                "Ana Perez",
                new Email("ana@uq.edu.co"),
                TipoUsuario.ESTUDIANTE
        );
        var response = new UsuarioResumenResponse("123456", "Ana Perez", "ESTUDIANTE", "ACTIVO");

        when(listarUsuariosUseCase.ejecutar(any()))
                .thenReturn(new PageImpl<>(List.of(usuario), PageRequest.of(0, 10), 1));
        when(usuarioMapper.toResumenResponse(usuario)).thenReturn(response);

        mockMvc.perform(get("/api/v1/usuarios")
                        .with(authentication(authenticationFor("900001", "security.admin@uq.edu.co", "ADMINISTRADOR"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenido[0].id").value("123456"))
                .andExpect(jsonPath("$.totalElementos").value(1))
                .andExpect(jsonPath("$.primera").value(true));
    }

    private UsernamePasswordAuthenticationToken authenticationFor(String id, String email, String role) {
        var usuario = new UsuarioEntity();
        usuario.setId(id);
        usuario.setEmail(email);
        usuario.setPassword("$2a$10$abcdefghijklmnopqrstuv");
        usuario.setTipo(TipoUsuario.valueOf(role));
        usuario.setEstado(co.edu.uniquindio.proyecto.domain.valueobject.EstadoUsuario.ACTIVO);
        var principal = new CustomUserDetails(usuario);
        return new UsernamePasswordAuthenticationToken(
                principal,
                null,
                java.util.List.of(new SimpleGrantedAuthority("ROLE_" + role))
        );
    }
}
