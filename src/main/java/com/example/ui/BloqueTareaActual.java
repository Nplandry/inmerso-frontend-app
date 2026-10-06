package com.example.ui;

import com.example.domain.Tarea;
import com.vaadin.flow.component.html.H3;

public final class BloqueTareaActual extends BloqueTarea {
    public BloqueTareaActual(Tarea tarea) {
        this(tarea, tarea.getTiempoRestanteMinutos());
    }

    public BloqueTareaActual(Tarea tarea, int minutosRestantes) {
        H3 cuentaRegresiva = new H3(
            "Termina en: " + minutosRestantes + " MIN"
        );
        cuentaRegresiva.addClassName("task-countdown");

        add(
            encabezado("HACIENDO AHORA"),
            titulo(tarea),
            descripcion(tarea),
            cuentaRegresiva
        );
        addClassNames("focus-block", "focus-block--current");
    }

    private H3 descripcion(Tarea tarea) {
        H3 descripcion = new H3("Descripción: " + tarea.getDescripcion());
        descripcion.addClassName("next-task-desc");
        return descripcion;
    }
}
