package com.InmersoAI.infraestructure.security;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import com.InmersoAI.domain.Usuario;
import com.InmersoAI.domain.UsuarioRepositoryPort;

@Repository
public class SecurityAdapter implements UsuarioRepositoryPort {
    private final SecurityEntityRepository repository;

    public SecurityAdapter(SecurityEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        SecurityEntity entity = usuario.getId() == null
                ? new SecurityEntity()
                : repository.findById(usuario.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        entity.setUsername(usuario.getUsername());
        entity.setPassword(usuario.getPassword());
        entity.setRole(usuario.getRole());
        entity.setOnboardingCompletado(usuario.isOnboardingCompletado());
        return toDomain(repository.save(entity));
    }

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        return repository.findByUsername(username).map(this::toDomain);
    }

    @Override
    public boolean existePorUsername(String username) {
        return repository.existsByUsername(username);
    }

    private Usuario toDomain(SecurityEntity entity) {
        return new Usuario(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getRole(),
                entity.isOnboardingCompletado()
        );
    }
}