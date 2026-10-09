package com.example.ui;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;

public final class BloqueSinTareas extends BloqueTarea {
    public BloqueSinTareas() {
        add(new H1("SIN TAREAS"), new H2("No hay tareas para después"));
        addClassNames("focus-block--completed", "focus-unavailable-task");
    }
}
