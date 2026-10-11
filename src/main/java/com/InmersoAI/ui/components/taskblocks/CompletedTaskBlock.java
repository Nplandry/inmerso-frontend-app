package com.InmersoAI.ui.components.taskblocks;

import com.InmersoAI.domain.Tarea;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;

public final class CompletedTaskBlock extends TaskBlock {
    public CompletedTaskBlock(Tarea tarea) {
        add(new H1("COMPLETADO"), new H2(tarea.getDescripcion()));
        addClassName("focus-block--completed");
    }
}
