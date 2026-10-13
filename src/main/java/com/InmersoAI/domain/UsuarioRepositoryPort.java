package com.InmersoAI.domain;

import java.util.Optional;

public interface UsuarioRepositoryPort {
    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorUsername(String username);

    boolean existePorUsername(String username);
}