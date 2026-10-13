package com.InmersoAI.infraestructure.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.InmersoAI.domain.Usuario;
import com.InmersoAI.domain.UsuarioRepositoryPort;

@Configuration
public class UsuariosMock {

    @Bean
    public CommandLineRunner initDatabase(
            UsuarioRepositoryPort usuarioRepositoryPort,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            String clientUsername = "cliente_prueba";

            if (!usuarioRepositoryPort.existePorUsername(clientUsername)) {
                Usuario cliente = new Usuario(
                        null,
                        clientUsername,
                        passwordEncoder.encode("cliente123"),
                        Usuario.ROLE_USER,
                        false
                );
                usuarioRepositoryPort.guardar(cliente);
            }
        };
    }
}