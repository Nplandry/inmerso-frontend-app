package com.InmersoAI.app;

import org.springframework.stereotype.Service;
import com.InmersoAI.domain.FocusScheduleRequest;
import com.InmersoAI.domain.Schedule;
import com.InmersoAI.domain.UsuarioRepositoryPort;
import java.util.Optional;

@Service
public class CrearUsuarioUseCaseImpl {
    private final Autentificacion autentificacion;

    public CrearUsuarioUseCaseImpl(Autentificacion autentificacion) {
        this.autentificacion = autentificacion;
    }

    public void obtenerNombreUsuario(Autentificacion autentificacion) {
    }
}