package co.edu.uniquindio.proyecto.infrastructure.controller;

import co.edu.uniquindio.proyecto.application.dto.request.CrearUsuarioRequest;
import co.edu.uniquindio.proyecto.application.dto.response.UsuarioDetalleResponse;
import co.edu.uniquindio.proyecto.application.dto.response.UsuarioResumenResponse;
import co.edu.uniquindio.proyecto.application.usecase.CrearUsuarioUseCase;
import co.edu.uniquindio.proyecto.application.usecase.ConsultarUsuarioPorIdUseCase;
import co.edu.uniquindio.proyecto.application.usecase.ListarUsuariosUseCase;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import co.edu.uniquindio.proyecto.infrastructure.mapper.UsuarioMapper;
import co.edu.uniquindio.proyecto.infrastructure.mapper.UsuarioRequestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gestión de usuarios.
 */
@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final CrearUsuarioUseCase crearUsuarioUseCase;
    private final ConsultarUsuarioPorIdUseCase consultarUsuarioPorIdUseCase;
    private final ListarUsuariosUseCase listarUsuariosUseCase;
    private final UsuarioMapper usuarioMapper;
    private final UsuarioRequestMapper usuarioRequestMapper;

    @PostMapping
    public ResponseEntity<UsuarioDetalleResponse> crearUsuario(
            @Valid @RequestBody CrearUsuarioRequest request) {

        var datos = usuarioRequestMapper.toDomainData(request);
        IdUsuario id = IdUsuario.generar();

        var usuario = crearUsuarioUseCase.ejecutar(
                id,
                datos.nombre(),
                datos.email(),
                datos.tipo()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioMapper.toDetalleResponse(usuario));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResumenResponse>> listarUsuarios() {

        var usuarios = listarUsuariosUseCase.ejecutar();

        return ResponseEntity.ok(usuarioMapper.toResumenResponseList(usuarios));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDetalleResponse> obtenerPorId(
            @PathVariable String id) {

        var usuario = consultarUsuarioPorIdUseCase.ejecutar(new IdUsuario(id));

        return ResponseEntity.ok(usuarioMapper.toDetalleResponse(usuario));
    }
}
