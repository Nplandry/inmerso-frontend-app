package com.InmersoAI.ui;
import com.InmersoAI.app.ScheduleAppService;
import com.InmersoAI.app.TareaAppService;
import com.InmersoAI.domain.Schedule;
import com.InmersoAI.domain.Tarea;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;

import jakarta.annotation.PostConstruct;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

@Route("")
public class MainPage extends VerticalLayout {
    
    private final TareaAppService tareaService;
    private final ScheduleAppService scheduleService;

    private final VerticalLayout bloqueActual = new VerticalLayout();
    private final VerticalLayout bloqueProximo = new VerticalLayout();
    private Tarea tareaActual;
    private Integer tiempoActual;
    private final Button listoButton;
    private final Button agregarTiempoButton;


    public MainPage(TareaAppService tareaService, ScheduleAppService scheduleService) {
        this.tareaService = tareaService;
        this.scheduleService = scheduleService;
        addClassName("main-view");
        bloqueProximo.setPadding(false); 
    
        listoButton = new Button("Listo");
        listoButton.addClassNames("action-btn", "action-btn--primary");
        listoButton.setEnabled(false);
        listoButton.addClickShortcut(Key.ENTER);

        agregarTiempoButton = new Button("+15 MIN");
        agregarTiempoButton.addClassNames("action-btn", "action-btn--secondary");
        agregarTiempoButton.setEnabled(false);

        /*###IGNORAR: DEF VARIABLES ESTATICOS ###*/
        var appTitle = new H1("Inmerso");
        appTitle.addClassName("app-title");
        var nuevaTarea = new Button("Nueva Tarea");
        nuevaTarea.addClassNames("action-btn", "action-btn--voice");
        var todosLayout = new VerticalLayout();
        todosLayout.addClassName("todos-container");
        bloqueActual.setPadding(false);
        var bloqueControles = new HorizontalLayout(agregarTiempoButton, listoButton);
        bloqueControles.addClassName("controls-container");

        var skeletonBloque = new VerticalLayout();
        skeletonBloque.addClassName("SkeletonBlock");
        /*###FIN IGNORAR: DEF VARIABLES ESTATICOS ###*/

        //#######--[Listeners]----#######
        nuevaTarea.addClickListener(click -> {
            abrirModal();
        });

        listoButton.addClickListener(click -> completarTareaActual());
        agregarTiempoButton.addClickListener(click -> agregarTiempoATareaActual(15));
    //#######----[END Listeners]----#######
    

    add(appTitle, skeletonBloque, bloqueActual, bloqueProximo, bloqueControles, nuevaTarea);}

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
        scheduleActualizado.ifPresentOrElse(
                this::actualizarBloqueSiguiente,
                this::actualizarBloqueSiguiente
        );
        dialog.close();
    }

    private void actualizarBloqueSiguiente() {

        scheduleService.obtenerSchedule().ifPresentOrElse(
                this::actualizarBloqueSiguiente,
                () -> actualizarBloqueSiguiente(null)
        );
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
        bloqueActual.add(new BloqueTareaCompletada(tareaActual));
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
    @PostConstruct
    public void cargarTareaDelBackend(){
        scheduleService.obtenerSchedule().ifPresentOrElse(schedule -> {
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
        }, () -> {
            tareaActual = null;
            tiempoActual = null;
            actualizarBloqueActual();
            actualizarBloqueSiguiente(null);
        });
    }
    /*######--FIN LOGICA DEL BACKEND [se ejecuta al cargar la pagina]--######*/

    private Component crearBloque(Tarea tarea) {
        return tarea == null
                ? new BloqueSinTareas()
                : new BloqueTareaActual(tarea, tiempoVisible(tarea));
    }
    private Component crearBloqueProximo(Schedule schedule) {
        return new BloqueProximaTarea(schedule);
    }
    private int tiempoVisible(Tarea tarea) {
        return tiempoActual != null
                ? tiempoActual
                : tarea.getTiempoRestanteMinutos();
    }
    public Component NoBloquesSiguentesDisponibles() {
        return new BloqueSinTareas();
    }
}