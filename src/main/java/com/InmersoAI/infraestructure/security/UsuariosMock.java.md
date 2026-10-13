package com.InmersoAI.infraestructure.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.InmersoAI.domain.Usuario;
import com.InmersoAI.domain.UsuarioRepositoryPort;

@Configuration
public class DataInitializerConfig {

    @Bean
    public CommandLineRunner initDatabase(UsuarioRepositoryPort usuarioRepositoryPort) {
        return args -> {
            String clientUsername = "cliente_prueba";

            if (!usuarioRepositoryPort.existePorUsername(clientUsername)) {
                Usuario cliente = new Usuario(
                        null,
                        clientUsername,
                        "cliente123", 
                        Usuario.ROLE_USER
                );
                usuarioRepositoryPort.guardar(cliente);
            }
        };
    }
}