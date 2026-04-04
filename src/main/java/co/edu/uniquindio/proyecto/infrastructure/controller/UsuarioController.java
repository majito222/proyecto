package co.edu.uniquindio.proyecto.infrastructure.controller;

import co.edu.uniquindio.proyecto.application.usecase.CrearUsuarioUseCase;
import co.edu.uniquindio.proyecto.application.usecase.ConsultarUsuarioPorIdUseCase;
import co.edu.uniquindio.proyecto.application.usecase.ListarUsuariosUseCase;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import co.edu.uniquindio.proyecto.infrastructure.dto.*;
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
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final CrearUsuarioUseCase crearUsuarioUseCase;
    private final ConsultarUsuarioPorIdUseCase consultarUsuarioPorIdUseCase;
    private final ListarUsuariosUseCase listarUsuariosUseCase;
    private final UsuarioMapper usuarioMapper;
    private final UsuarioRequestMapper usuarioRequestMapper;

    @PostMapping
    public ResponseEntity<UsuarioResponse> crearUsuario(
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
                .body(usuarioMapper.toResponse(usuario));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {

        var usuarios = listarUsuariosUseCase.ejecutar();

        return ResponseEntity.ok(usuarioMapper.toResponseList(usuarios));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerPorId(
            @PathVariable String id) {

        var usuario = consultarUsuarioPorIdUseCase.ejecutar(new IdUsuario(id));

        return ResponseEntity.ok(usuarioMapper.toResponse(usuario));
    }
}
