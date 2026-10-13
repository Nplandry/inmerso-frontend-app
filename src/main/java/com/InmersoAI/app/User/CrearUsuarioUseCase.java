package com.InmersoAI.app.User;

import com.InmersoAI.domain.Usuario;
import java.util.Optional;

public interface CrearUsuarioUseCase {
    Usuario crearUsuario(String username, String password);

    Optional<Usuario> buscarPorUsername(String username);

    Usuario completarOnboarding(String username);
}