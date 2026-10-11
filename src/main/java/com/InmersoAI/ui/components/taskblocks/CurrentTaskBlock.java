package com.InmersoAI.ui.components.taskblocks;

import com.InmersoAI.domain.Tarea;
import com.vaadin.flow.component.html.H3;

public final class CurrentTaskBlock extends TaskBlock {
    public CurrentTaskBlock(Tarea tarea) {
        this(tarea, tarea.getTiempoRestanteMinutos());
    }

    public CurrentTaskBlock(Tarea tarea, int minutosRestantes) {
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
