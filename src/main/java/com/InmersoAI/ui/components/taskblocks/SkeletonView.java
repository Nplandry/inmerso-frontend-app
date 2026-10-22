package com.InmersoAI.ui.components.taskblocks;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

//Skeleton animado con efecto shimmer. Usando la estructura de {@code FocusView}:
public final class SkeletonView extends VerticalLayout {

    public SkeletonView() {
        setPadding(false);
        setSpacing(false);
        addClassName("skeleton-view");
        setWidthFull();

        add(crearBloqueUltimo(), crearBloqueActual(), crearBloqueProximo(), crearControles(), crearAcciones());
    }

    // Actividad anterior
    private VerticalLayout crearBloqueUltimo() {
        VerticalLayout bloque = new VerticalLayout();
        bloque.setPadding(false);
        bloque.addClassNames("focus-block", "focus-block--next", "skeleton-block");

        bloque.add(
                linea("skeleton-line--chip"),    
                linea("skeleton-line--title"),    
                linea("skeleton-line--countdown")
        );
        return bloque;
    }

    // Bloque superior
    private VerticalLayout crearBloqueActual() {
        VerticalLayout bloque = new VerticalLayout();
        bloque.setPadding(false);
        bloque.addClassNames("focus-block", "focus-block--current", "skeleton-block");

        bloque.add(
                linea("skeleton-line--chip"),     
                linea("skeleton-line--title"),    
                linea("skeleton-line--text"),    
                linea("skeleton-line--countdown") 
        );
        return bloque;
    }

    // Bloque interior
    private VerticalLayout crearBloqueProximo() {
        VerticalLayout bloque = new VerticalLayout();
        bloque.setPadding(false);
        bloque.addClassNames("focus-block", "focus-block--next", "skeleton-block");

        bloque.add(
                linea("skeleton-line--chip"),     
                linea("skeleton-line--title"),   
                linea("skeleton-line--countdown") 
        );
        return bloque;
    }

    // Fila de controles
    private HorizontalLayout crearControles() {
        HorizontalLayout fila = new HorizontalLayout();
        fila.addClassNames("controls-container", "skeleton-controls");
        fila.add(linea("skeleton-line--button"), linea("skeleton-line--button"));
        return fila;
    }

    // Fila de acciones
    private HorizontalLayout crearAcciones() {
        HorizontalLayout fila = new HorizontalLayout();
        fila.addClassNames("controls-container", "skeleton-controls");
        fila.add(linea("skeleton-line--button"), linea("skeleton-line--button-wide"));
        return fila;
    }

    private Div linea(String modificador) {
        Div div = new Div();
        div.addClassNames("skeleton-line", modificador);
        return div;
    }
}
