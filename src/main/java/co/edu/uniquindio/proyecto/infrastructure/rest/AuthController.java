package co.edu.uniquindio.proyecto.infrastructure.rest;

import co.edu.uniquindio.proyecto.application.dto.request.LoginRequest;
import co.edu.uniquindio.proyecto.application.dto.response.LoginResponse;
import co.edu.uniquindio.proyecto.infrastructure.security.jwt.JwtService;
import co.edu.uniquindio.proyecto.infrastructure.security.service.SecurityServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para autenticación (login).
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final JwtService jwtService;
    private final SecurityServiceImpl securityService;

    /**
     * Login de usuario.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        // Cargar usuario (esto valida que exista)
        var userDetails = securityService.loadUserByUsername(request.usuarioId());

        //  Generar token
        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(
                new LoginResponse(token, "Bearer")
        );
    }
}
