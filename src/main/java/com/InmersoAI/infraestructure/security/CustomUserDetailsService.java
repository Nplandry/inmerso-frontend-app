package com.InmersoAI.infraestructure.security;

import com.InmersoAI.domain.UsuarioRepositoryPort;
import com.InmersoAI.domain.model.Usuario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UsuarioRepositoryPort usuarioRepository;

    public CustomUserDetailsService(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("[Auth] Intento de login para usuario='{}'", username);

        Usuario usuario = usuarioRepository.buscarPorUsername(username)
                .orElseThrow(() -> {
                    log.warn("[Auth] Usuario '{}' NO encontrado en el repositorio.", username);
                    return new UsernameNotFoundException("Usuario no encontrado");
                });

        log.info("[Auth] Usuario '{}' encontrado. role={} onboardingCompletado={}. Verificando password...",
                usuario.getUsername(), usuario.getRole(), usuario.isOnboardingCompletado());

        return User.withUsername(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(usuario.getRole())
                .build();
    }
}
