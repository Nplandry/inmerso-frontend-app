package com.InmersoAI.ui.views.focus;

import com.InmersoAI.app.Tarea.ScheduleAppService;
import com.InmersoAI.app.Tarea.TareaAppService;
import com.InmersoAI.domain.model.Schedule;
import com.InmersoAI.domain.model.Tarea;
import com.InmersoAI.ui.layouts.MainLayout;
import com.InmersoAI.ui.session.SesionUsuario;
import com.InmersoAI.ui.views.auth.LoginView;
import com.InmersoAI.ui.views.auth.Onboarding;
import com.InmersoAI.ui.components.taskblocks.CompletedTaskBlock;
import com.InmersoAI.ui.components.taskblocks.CurrentTaskBlock;
import com.InmersoAI.ui.components.taskblocks.EmptyTaskBlock;
import com.InmersoAI.ui.components.taskblocks.NextTaskBlock;
import com.InmersoAI.ui.components.taskblocks.SkeletonView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;

import jakarta.annotation.security.DenyAll;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;



@Route(value = "", layout = MainLayout.class)
@PageTitle("Inicio")
@DenyAll
public class FocusView extends VerticalLayout implements BeforeEnterObserver {
    
    private final TareaAppService tareaService;
    private final ScheduleAppService scheduleService;
    private final SesionUsuario sesion;

    private final VerticalLayout bloqueActual = new VerticalLayout();
    private final VerticalLayout bloqueProximo = new VerticalLayout();
    private final SkeletonView skeleton = new SkeletonView();
    private Tarea tareaActual;
    private Integer tiempoActual;
    private final Button listoButton;
    private final Button agregarTiempoButton;
    private HorizontalLayout bloqueControles;
    private HorizontalLayout bloquesEntrada;
    private VerticalLayout bloqueUltimo;


    public FocusView(TareaAppService tareaService, ScheduleAppService scheduleService, SesionUsuario sesion) {
        this.tareaService = tareaService;
        this.scheduleService = scheduleService;
        this.sesion = sesion;
        addClassName("main-view");
        bloqueProximo.setPadding(false); 
    
        listoButton = new Button("Listo");
        listoButton.addClassNames("action-btn", "action-btn--primary");
        listoButton.setEnabled(false);
        listoButton.addClickShortcut(Key.ENTER);

        agregarTiempoButton = new Button("+15 MIN");
        agregarTiempoButton.addClassNames("action-btn", "action-btn--secondary");
        agregarTiempoButton.setEnabled(false);

        var snapFocus = new Button("Grabar");
        var nuevaTarea = new Button("Nueva Tarea");
        nuevaTarea.addClassNames("action-btn", "action-btn--text");
        snapFocus.addClassNames("action-btn", "action-btn--voice");
        var bloquesEntrada = new HorizontalLayout(nuevaTarea, snapFocus);
        bloquesEntrada.addClassName("controls-container");
        var todosLayout = new VerticalLayout();
        todosLayout.addClassName("todos-container");
        bloqueActual.setPadding(false);
        var bloqueControles = new HorizontalLayout(agregarTiempoButton, listoButton);
        bloqueControles.addClassName("controls-container");

        var skeletonBloque = new VerticalLayout();
        skeletonBloque.addClassNames("SkeletonBlock", "focus-block", "focus-block--next");
        H2 ultimo = new H2("ULTIMO");
        ultimo.addClassName("block-title");
        H2 tareaUltima = new H2("Estudiar una Hora");
        tareaUltima.addClassName("task-title");
        H3 terminaEn = new H3("Termina en: 3 MIN");
        terminaEn.addClassName("task-countdown");
        skeletonBloque.add(ultimo, tareaUltima, terminaEn);

        //#######--[Listeners]----#######
        nuevaTarea.addClickListener(click -> {
            abrirModal();
        });

        listoButton.addClickListener(click -> completarTareaActual());
        agregarTiempoButton.addClickListener(click -> agregarTiempoATareaActual(15));
    //#######----[END Listeners]----#######

    // El contenido real empieza oculto; se muestra cuando llega realmente el schedule.
    // Mientras tanto se ve el skeleton animado 
    bloqueActual.setVisible(false);
    bloqueProximo.setVisible(false);
    bloqueControles.setVisible(false);
    bloquesEntrada.setVisible(false);
    skeletonBloque.setVisible(false);
    this.bloqueControles = bloqueControles;
    this.bloquesEntrada = bloquesEntrada;
    this.bloqueUltimo = skeletonBloque;

    add(skeletonBloque, skeleton, bloqueActual, bloqueProximo, bloqueControles, bloquesEntrada);

    }
    
    
    /*######--LOGICA DEL MODAL--######*/
    public void abrirModal() {
        Dialog dialog = new Dialog();
           dialog.addClassName("modal-container"); 
        dialog.setCloseOnOutsideClick(false);
        dialog.setHeaderTitle("Ingrese su información");

        TextField inputNombre = new TextField("Descripción de la tarea");
        inputNombre.setPlaceholder("Estudiar en una hora más");
        inputNombre.setWidthFull();

        Button botonGuardar = new Button("Guardar");
        botonGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        botonGuardar.addClickListener(event -> guardarTarea(inputNombre, dialog));
        inputNombre.addKeyDownListener(Key.ENTER, event -> botonGuardar.click());

        Button botonCancelar = new Button("Cancelar", event -> dialog.close());
        VerticalLayout dialogLayout = new VerticalLayout(inputNombre);
        dialog.add(dialogLayout);
        dialog.getFooter().add(botonCancelar, botonGuardar);
        dialog.open();
    }

    private void guardarTarea(TextField input, Dialog dialog) {
        String descripcion = input.getValue().trim();
        if (descripcion.isBlank()) {
            input.setErrorMessage("La descripción es obligatoria");
            input.setInvalid(true);
            return;
        }

        var scheduleActualizado = scheduleService.actualizarSchedule(descripcion);
        tareaService.crearTarea(descripcion);
        // Usamos el Schedule que ya devuelve el POST...
        actualizarBloqueSiguiente(scheduleActualizado.orElse(null));
        dialog.close();
    }

    private void actualizarBloqueSiguiente(Schedule schedule) {
        bloqueProximo.removeAll();
        bloqueProximo.add(crearBloqueProximo(schedule));
    }

    private void actualizarBloqueActual() {
        bloqueActual.removeAll();
        boolean hayTarea = tareaActual != null;
        listoButton.setEnabled(hayTarea);
        agregarTiempoButton.setEnabled(hayTarea);
        if (hayTarea) {
            bloqueActual.add(crearBloque(tareaActual));
        } else {
            bloqueActual.add(crearBloqueActualVacio());
        }
    }

    private Component crearBloqueActualVacio() {
        H1 estado = new H1("HACIENDO AHORA");
        HorizontalLayout encabezado = new HorizontalLayout(estado);
        encabezado.addClassName("block-header");

        H2 mensaje = new H2("No hay tarea en curso");
        mensaje.addClassNames("task-title", "empty-task-message");

        VerticalLayout bloqueVacio = new VerticalLayout(encabezado, mensaje);
        bloqueVacio.setPadding(false);
        bloqueVacio.addClassNames("focus-block", "focus-block--current");
        return bloqueVacio;
    }

    private void completarTareaActual() {
        if (tareaActual == null) {
            return;
        }
        tareaService.completarTarea(tareaActual.getId())
                .ifPresent(tarea -> tareaActual = tarea);
        bloqueActual.removeAll();
        bloqueActual.add(new CompletedTaskBlock(tareaActual));
        listoButton.setEnabled(false);
        agregarTiempoButton.setEnabled(false);
    }

    private void agregarTiempoATareaActual(int minutos) {
        if (tareaActual == null) {
            return;
        }
        tareaService.agregarTiempo(tareaActual.getId(), minutos)
                .ifPresent(tarea -> {
                    tareaActual = tarea;
                    tiempoActual = tarea.getTiempoRestanteMinutos();
                });
        actualizarBloqueActual();
    }
    /*######--FIN: LOGICA DEL MODAL--######*/
    /**
    Al adjuntarse la vista mostramos de inmediato el skeleton y se dispara la carga del schedule en un hilo aparte. Cuando llega
    la respuesta del backend externo se actualizara la UI con {@link UI#access}
     */
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        final UI ui = attachEvent.getUI();
        new Thread(() -> {
            // Llamada de red fuera del hilo de la UI.
            var scheduleOpt = scheduleService.obtenerSchedule();
            // Aplicamos el resultado dentro del lock de sesion de Vaadin.
            ui.access(() -> aplicarSchedule(scheduleOpt.orElse(null)));
        }, "focus-schedule-loader").start();
    }

    /**
     * Refleja en la UI el schedule recibido del backend
     * Debe invocarse siempre dentro de {@link UI#access}.
     */
    private void aplicarSchedule(Schedule schedule) {
        if (schedule != null) {
            String tituloTareaActual = schedule.currentTaskTitle();
            if (tituloTareaActual != null && !tituloTareaActual.isBlank()) {
                tareaActual = tareaService.crearTarea(tituloTareaActual);
                tiempoActual = schedule.currentEndsInMinutes() == null
                        ? 0
                        : schedule.currentEndsInMinutes();
            } else {
                tareaActual = null;
                tiempoActual = null;
            }
            actualizarBloqueActual();
            actualizarBloqueSiguiente(schedule);
        } else {
            tareaActual = null;
            tiempoActual = null;
            actualizarBloqueActual();
            actualizarBloqueSiguiente(null);
        }
        mostrarContenido();
    }

    /** Oculta el skeleton animado y revela el contenido real de la vista. */
    private void mostrarContenido() {
        skeleton.setVisible(false);
        bloqueUltimo.setVisible(true);
        bloqueActual.setVisible(true);
        bloqueProximo.setVisible(true);
        bloqueControles.setVisible(true);
        bloquesEntrada.setVisible(true);
    }
    /*######--FIN LOGICA DEL BACKEND [carga asincrona cuando se adjunta la vista]--######*/

    private Component crearBloque(Tarea tarea) {
        return tarea == null
                ? new EmptyTaskBlock()
                : new CurrentTaskBlock(tarea, tiempoVisible(tarea));
    }
    private Component crearBloqueProximo(Schedule schedule) {
        return new NextTaskBlock(schedule);
    }
    private int tiempoVisible(Tarea tarea) {
        return tiempoActual != null
                ? tiempoActual
                : tarea.getTiempoRestanteMinutos();
    }
    public Component NoBloquesSiguentesDisponibles() {
        return new EmptyTaskBlock();
    }
    

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!sesion.estaAutenticado()) {
            event.forwardTo(LoginView.class);
        } else if(sesion.necesitaOnboarding()) {
            event.forwardTo(Onboarding.class);
        }
    }
}