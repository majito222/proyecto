package co.edu.uniquindio.proyecto.infrastructure.controller;

import co.edu.uniquindio.proyecto.application.usecase.CrearUsuarioUseCase;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
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
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final UsuarioRequestMapper usuarioRequestMapper;

    /**
     * Crea un nuevo usuario.
     */
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

    /**
     * Lista todos los usuarios.
     */
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        var usuarios = usuarioRepository.listarTodos();
        return ResponseEntity.ok(usuarioMapper.toResponseList(usuarios));
    }

    /**
     * Obtiene un usuario por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerPorId(
            @PathVariable String id) {
        try {
            var usuario = usuarioRepository.buscarPorId(new IdUsuario(id));
            return ResponseEntity.ok(usuarioMapper.toResponse(usuario));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Activa un usuario.
     */
    @PutMapping("/{id}/activar")
    public ResponseEntity<Void> activarUsuario(@PathVariable String id) {
        try {
            var usuario = usuarioRepository.buscarPorId(new IdUsuario(id));
            crearUsuarioUseCase.activarUsuario(usuario);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Desactiva un usuario.
     */
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarUsuario(@PathVariable String id) {
        try {
            var usuario = usuarioRepository.buscarPorId(new IdUsuario(id));
            crearUsuarioUseCase.desactivarUsuario(usuario);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
