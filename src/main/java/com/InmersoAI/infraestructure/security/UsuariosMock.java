package com.InmersoAI.infraestructure.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.InmersoAI.domain.UsuarioRepositoryPort;
import com.InmersoAI.domain.model.Usuario;

@Configuration
public class UsuariosMock {

    private static final Logger log = LoggerFactory.getLogger(UsuariosMock.class);

    @Bean
    public CommandLineRunner initDatabase(
            UsuarioRepositoryPort usuarioRepositoryPort,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            // Usuario nuevo: onboarding pendiente -> deberia ir al Onboarding
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
                log.info("[UsuariosMock] Creado usuario mock '{}' (onboarding PENDIENTE). Password: cliente123", clientUsername);
            } else {
                log.info("[UsuariosMock] Usuario mock '{}' ya existe. No se recrea.", clientUsername);
            }

            // si el oboarding esta completado -> deberia ir al FocusView
            String veteranoUsername = "cliente_listo";
            if (!usuarioRepositoryPort.existePorUsername(veteranoUsername)) {
                Usuario veterano = new Usuario(
                        null,
                        veteranoUsername,
                        passwordEncoder.encode("cliente123"),
                        Usuario.ROLE_USER,
                        true
                );
                usuarioRepositoryPort.guardar(veterano);
                log.info("[UsuariosMock] Creado usuario mock '{}' (onboarding COMPLETADO). Password: cliente123", veteranoUsername);
            } else {
                log.info("[UsuariosMock] Usuario mock '{}' ya existe. No se recrea.", veteranoUsername);
            }
        };
    }
}
