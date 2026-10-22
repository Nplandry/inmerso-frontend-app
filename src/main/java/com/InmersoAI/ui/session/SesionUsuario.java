package com.InmersoAI.ui.session;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.InmersoAI.app.User.CrearUsuarioUseCase;
import com.InmersoAI.domain.model.Usuario;
import com.vaadin.flow.spring.security.AuthenticationContext;

/**
 * Puente entre las capas de usuario dominio -> aplicacion -> seguridad y la capa de presentacion.
 *
 * Tiene alcance de request: el {@link Usuario} autenticado se resuelve una sola vez por
 * peticion HTTP y se cachea, evitando consultas repetidas al backend cuando
 * varios componentes preguntan por el estado de la sesion en el mismo render.
 */
@Component
@RequestScope
public class SesionUsuario {

    private static final Logger log = LoggerFactory.getLogger(SesionUsuario.class);

    private final AuthenticationContext authenticationContext;
    private final CrearUsuarioUseCase usuarioUseCase;

    // Cache por request -> null = aun no resuelto en esta peticion.
    private Optional<Usuario> usuarioCacheado;

    public SesionUsuario(AuthenticationContext authenticationContext,
                         CrearUsuarioUseCase usuarioUseCase) {
        this.authenticationContext = authenticationContext;
        this.usuarioUseCase = usuarioUseCase;
    }

    //true si hay un usuario autenticado en la sesion actual. 
    public boolean estaAutenticado() {
        boolean autenticado = authenticationContext.isAuthenticated();
        log.debug("[SesionUsuario] estaAutenticado={}", autenticado);
        return autenticado;
    }

    // get del usuario autenticado, si existe. 
    public Optional<String> usernameAutenticado() {
        Optional<String> username = authenticationContext.getPrincipalName();
        log.debug("[SesionUsuario] usernameAutenticado={}", username.orElse("<ninguno>"));
        return username;
    }

    // Usuario de dominio autenticado que incluye estado de onboarding, si es que existe.
    // Se resuelve una vez por request y se reutiliza en las siguientes llamadas.
    public Optional<Usuario> usuarioActual() {
        if (usuarioCacheado != null) {
            return usuarioCacheado;
        }
        Optional<Usuario> usuario = usernameAutenticado().flatMap(usuarioUseCase::buscarPorUsername);
        if (usuario.isPresent()) {
            log.debug("[SesionUsuario] usuarioActual='{}' onboardingCompletado={}",
                    usuario.get().getUsername(), usuario.get().isOnboardingCompletado());
        } else {
            log.debug("[SesionUsuario] usuarioActual=<ninguno>");
        }
        usuarioCacheado = usuario;
        return usuario;
    }

    // true si hay usuario autenticado pero aun no completo el onboarding. 
    public boolean necesitaOnboarding() {
        boolean necesita = usuarioActual()
                .map(usuario -> !usuario.isOnboardingCompletado())
                .orElse(false);
        log.debug("[SesionUsuario] necesitaOnboarding={}", necesita);
        return necesita;
    }

    // true si hay usuario autenticado y ya completo el onboarding.
    public boolean onboardingCompletado() {
        boolean completado = usuarioActual()
                .map(Usuario::isOnboardingCompletado)
                .orElse(false);
        log.debug("[SesionUsuario] onboardingCompletado={}", completado);
        return completado;
    }
}
