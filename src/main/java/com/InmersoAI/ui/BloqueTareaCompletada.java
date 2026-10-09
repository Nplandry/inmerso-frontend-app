package com.InmersoAI.ui;

import com.InmersoAI.domain.Tarea;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;

public final class BloqueTareaCompletada extends BloqueTarea {
    public BloqueTareaCompletada(Tarea tarea) {
        add(new H1("COMPLETADO"), new H2(tarea.getDescripcion()));
        addClassName("focus-block--completed");
    }
}
