package co.edu.uniquindio.proyecto.infrastructure.security;

import co.edu.uniquindio.proyecto.domain.valueobject.EstadoUsuario;
import co.edu.uniquindio.proyecto.infrastructure.jpa.UsuarioEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record CustomUserDetails(UsuarioEntity usuario) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getTipo().name()));
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return EstadoUsuario.ACTIVO.equals(usuario.getEstado());
    }

    @Override
    public boolean isAccountNonLocked() {
        return EstadoUsuario.ACTIVO.equals(usuario.getEstado());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return EstadoUsuario.ACTIVO.equals(usuario.getEstado());
    }
}