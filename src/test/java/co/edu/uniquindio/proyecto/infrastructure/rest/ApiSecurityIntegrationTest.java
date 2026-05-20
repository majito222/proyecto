package co.edu.uniquindio.proyecto.infrastructure.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void endpointProtegidoSinAutenticacionDebeRetornarUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/health"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.codigo").value("NO_AUTENTICADO"))
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    @WithMockUser(roles = "ESTUDIANTE")
    void endpointDeAdministracionConRolInsuficienteDebeRetornarForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.codigo").value("NO_AUTORIZADO"))
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void endpointProtegidoConRolPermitidoDebeRetornarOk() throws Exception {
        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk());
    }
}
