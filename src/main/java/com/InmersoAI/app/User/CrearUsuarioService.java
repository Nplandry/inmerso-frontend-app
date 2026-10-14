package com.InmersoAI.app.User;

import com.InmersoAI.domain.UsuarioRepositoryPort;
import com.InmersoAI.domain.model.Usuario;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class CrearUsuarioService implements CrearUsuarioUseCase {
    private final UsuarioRepositoryPort usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public CrearUsuarioService(UsuarioRepositoryPort usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario crearUsuario(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        String normalizedUsername = username.trim();
        if (usuarioRepository.existePorUsername(normalizedUsername)) {
            throw new IllegalArgumentException("El nombre de usuario ya está registrado");
        }

        Usuario usuario = new Usuario(
                null,
                normalizedUsername,
                passwordEncoder.encode(password),
                Usuario.ROLE_USER,
                false
        );
        return usuarioRepository.guardar(usuario);
    }

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.buscarPorUsername(username);
    }

    @Override
    public Usuario completarOnboarding(String username) {
        Usuario usuario = usuarioRepository.buscarPorUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        usuario.completarOnboarding();
        return usuarioRepository.guardar(usuario);
    }
}