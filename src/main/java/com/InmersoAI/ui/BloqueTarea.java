package com.InmersoAI.ui;

import com.InmersoAI.domain.Tarea;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class BloqueTarea extends VerticalLayout {
    protected BloqueTarea() {
        setPadding(false);
    }

    protected HorizontalLayout encabezado(String titulo) {
        H1 estado = new H1(titulo);

        HorizontalLayout encabezado = new HorizontalLayout(estado);
        encabezado.addClassName("block-header");
        return encabezado;
    }

    protected H2 titulo(Tarea tarea) {
        H2 titulo = new H2(tarea.getDescripcion());
        titulo.addClassName("task-title");
        return titulo;
    }
}
