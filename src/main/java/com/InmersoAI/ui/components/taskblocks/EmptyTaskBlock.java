package com.InmersoAI.ui.components.taskblocks;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;

public final class EmptyTaskBlock extends TaskBlock {
    public EmptyTaskBlock() {
        add(new H1("SIN TAREAS"), new H2("No hay tareas para después"));
        addClassNames("focus-block--completed", "focus-unavailable-task");
    }
}
