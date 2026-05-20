package co.edu.uniquindio.proyecto.infrastructure.security.service;

import co.edu.uniquindio.proyecto.infrastructure.jpa.UsuarioJpaDataRepository;
import co.edu.uniquindio.proyecto.infrastructure.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements UserDetailsService {

    private final UsuarioJpaDataRepository usuarioJpaDataRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioJpaDataRepository.findByEmail(username)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }
}
