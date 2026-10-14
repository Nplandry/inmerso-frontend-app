package com.InmersoAI.app.User;

import java.util.Optional;

import com.InmersoAI.domain.model.Usuario;

public interface CrearUsuarioUseCase {
    Usuario crearUsuario(String username, String password);

    Optional<Usuario> buscarPorUsername(String username);

    Usuario completarOnboarding(String username);
}