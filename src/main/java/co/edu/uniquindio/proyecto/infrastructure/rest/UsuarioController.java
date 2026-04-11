package co.edu.uniquindio.proyecto.infrastructure.rest;

import co.edu.uniquindio.proyecto.application.dto.request.CrearUsuarioRequest;
import co.edu.uniquindio.proyecto.application.dto.response.ErrorResponse;
import co.edu.uniquindio.proyecto.application.dto.response.UsuarioDetalleResponse;
import co.edu.uniquindio.proyecto.application.dto.response.UsuarioResumenResponse;
import co.edu.uniquindio.proyecto.application.ConsultarUsuarioPorIdUseCase;
import co.edu.uniquindio.proyecto.application.CrearUsuarioUseCase;
import co.edu.uniquindio.proyecto.application.ListarUsuariosUseCase;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import co.edu.uniquindio.proyecto.infrastructure.mapper.UsuarioMapper;
import co.edu.uniquindio.proyecto.infrastructure.mapper.UsuarioRequestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Usuarios", description = "Operaciones REST del agregado Usuario")
public class UsuarioController {

    private final CrearUsuarioUseCase crearUsuarioUseCase;
    private final ConsultarUsuarioPorIdUseCase consultarUsuarioPorIdUseCase;
    private final ListarUsuariosUseCase listarUsuariosUseCase;
    private final UsuarioMapper usuarioMapper;
    private final UsuarioRequestMapper usuarioRequestMapper;

    @PostMapping
    @Operation(summary = "Crear usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<UsuarioDetalleResponse> crearUsuario(
            @Valid @RequestBody CrearUsuarioRequest request) {

        var datos = usuarioRequestMapper.toDomainData(request);
        var usuario = crearUsuarioUseCase.ejecutar(
                datos.nombre(),
                datos.email(),
                datos.tipo()
        );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(usuario.getId().valor())
                .toUri();

        return ResponseEntity.created(location)
                .body(usuarioMapper.toDetalleResponse(usuario));
    }

    @GetMapping
    @Operation(summary = "Listar usuarios")
    public ResponseEntity<List<UsuarioResumenResponse>> listarUsuarios() {

        var usuarios = listarUsuariosUseCase.ejecutar();

        return ResponseEntity.ok(usuarioMapper.toResumenResponseList(usuarios));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar usuario por id")
    public ResponseEntity<UsuarioDetalleResponse> obtenerPorId(
            @PathVariable String id) {

        var usuario = consultarUsuarioPorIdUseCase.ejecutar(new IdUsuario(id));

        return ResponseEntity.ok(usuarioMapper.toDetalleResponse(usuario));
    }
}
