package com.example.ui;

import com.example.domain.Tarea;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H3;

public final class BloqueProximaTarea extends BloqueTarea {

    public BloqueProximaTarea(Tarea tarea) {
        this(tarea, tarea.getTiempoRestanteMinutos());
    }

    public BloqueProximaTarea(Tarea tarea, int minutosRestantes) {
        Component tituloEncabezado = encabezado("PRÓXIMO BLOQUE");
        tituloEncabezado.addClassName("block-title");

        Component tituloTarea = titulo(tarea);
        tituloTarea.addClassName("task-title");

        H3 descTarea = descripcion(tarea);
        descTarea.addClassName("next-task-desc");

        H3 cuentaRegresiva = new H3("Empieza en: " + minutosRestantes + " MIN");
        cuentaRegresiva.addClassName("task-countdown");

        add(
            tituloEncabezado,
            tituloTarea,
            descTarea,
            cuentaRegresiva
        );

        setPadding(false);
        addClassNames("focus-block", "focus-block--next");
    }

    private H3 descripcion(Tarea tarea) {
        return new H3("Descripción: " + tarea.getDescripcion());
    }
}