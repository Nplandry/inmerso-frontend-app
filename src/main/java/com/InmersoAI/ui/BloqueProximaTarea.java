package com.InmersoAI.ui;
import com.InmersoAI.domain.Schedule;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;

public final class BloqueProximaTarea extends BloqueTarea {

    public BloqueProximaTarea(Schedule schedule) {
        H2 encabezado = new H2("PROXIMO BLOQUE");
        encabezado.addClassName("block-title");
        add(encabezado);

        String tituloActual = schedule == null ? null : schedule.currentTaskTitle();
        String tituloProximo = schedule == null ? null : schedule.nextTaskTitle();
        boolean hayProximo = tituloProximo != null
                && !tituloProximo.isBlank()
                && (tituloActual == null || tituloActual.isBlank()
                    || !tituloActual.trim().equalsIgnoreCase(tituloProximo.trim()));

        if (!hayProximo) {
            H3 sinTarea = new H3("No hay tareas para después");
            sinTarea.addClassNames("next-task-desc", "empty-task-message");
            add(sinTarea);
        } else {
            H3 tituloTarea = new H3(tituloProximo);
            tituloTarea.addClassName("task-title");

            int minutosHastaInicio = schedule.nextStartsInMinutes() == null
                    ? 0
                    : schedule.nextStartsInMinutes();
            H3 cuentaRegresiva = new H3("Empieza en: " + minutosHastaInicio + " MIN");
            cuentaRegresiva.addClassName("task-countdown");

            add(tituloTarea, cuentaRegresiva);
        }

        addClassNames("focus-block", "focus-block--next");
    }
}
