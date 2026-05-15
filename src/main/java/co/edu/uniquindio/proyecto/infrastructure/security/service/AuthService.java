package co.edu.uniquindio.proyecto.infrastructure.security.service;

import co.edu.uniquindio.proyecto.application.dto.request.LoginRequest;
import co.edu.uniquindio.proyecto.application.dto.response.LoginResponse;
import co.edu.uniquindio.proyecto.infrastructure.security.config.JwtConfig;
import co.edu.uniquindio.proyecto.infrastructure.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(token, "Bearer", jwtConfig.getExpiration());
    }
}
