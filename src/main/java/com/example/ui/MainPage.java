package com.example.ui;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.example.app.ScheduleAppService;
import com.example.app.TareaAppService;
import com.example.domain.Schedule;
import com.example.domain.Tarea;
import jakarta.annotation.PostConstruct;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
//import com.vaadin.flow.component.button.ButtonVariant;
//import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
//import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

@Route("")
public class MainPage extends VerticalLayout {
    
    private final TareaAppService tareaService;
    private final ScheduleAppService scheduleService;

    private final VerticalLayout bloqueActual = new VerticalLayout();
    private Tarea tareaActual;
    private Integer tiempoActual;
    private final Button listoButton;
    private final Button agregarTiempoButton;


    public MainPage(TareaAppService tareaService, ScheduleAppService scheduleService) {
        this.tareaService = tareaService;
        this.scheduleService = scheduleService;
        addClassName("main-view");
    
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
        /*###FIN IGNORAR: DEF VARIABLES ESTATICOS ###*/

        //#######--[Listeners]----#######
        nuevaTarea.addClickListener(click -> {
            abrirModal();
        });

        listoButton.addClickListener(click -> completarTareaActual());
        agregarTiempoButton.addClickListener(click -> agregarTiempoATareaActual(15));
    //#######----[END Listeners]----#######

    //####TEST####
    scheduleService.actualizarSchedule("Estudiar 1 Hora en 1 minuto mas");


    add(appTitle, bloqueActual, crearBloqueProximo(), bloqueControles, nuevaTarea);}

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

        tareaActual = tareaService.crearTarea(descripcion);
        tiempoActual = null;
        actualizarBloqueActual();
        dialog.close();
    }

    private void actualizarBloqueActual() {
        bloqueActual.removeAll();
        boolean hayTarea = tareaActual != null;
        listoButton.setEnabled(hayTarea);
        agregarTiempoButton.setEnabled(hayTarea);
        if (hayTarea) {
            bloqueActual.add(crearBloque(tareaActual));
        }
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

    /*######--LOGICA DEL BACKEND--######*/
    @PostConstruct
    public void cargarTareaDelBackend(){
        scheduleService.obtenerSchedule().ifPresent(schedule -> {
            String tituloTareaActual = schedule.currentTaskTitle();
            if (tituloTareaActual != null && !tituloTareaActual.isBlank()) {
                tareaActual = tareaService.crearTarea(tituloTareaActual);
                tiempoActual = schedule.currentEndsInMinutes() == null
                        ? 0
                        : schedule.currentEndsInMinutes();
                actualizarBloqueActual();
            }
        });
    }

    private Component crearBloque(Tarea tarea) {
        return tarea == null
                ? new BloqueSinTareas()
                : new BloqueTareaActual(tarea, tiempoVisible(tarea));
    }

    private int tiempoVisible(Tarea tarea) {
        return tiempoActual != null
                ? tiempoActual
                : tarea.getTiempoRestanteMinutos();
    }

    private Component crearBloqueProximo() {
        var schedule = scheduleService.obtenerSchedule();
        if (schedule.isEmpty() || schedule.get().nextTaskTitle() == null) {
            return NoBloquesSiguentesDisponibles();
        }

        Schedule nextSchedule = schedule.get();
        String siguienteTarea = !nextSchedule.nextTaskTitle().isBlank()
                ? nextSchedule.nextTaskTitle()
                : "Tarea Personalizada";
        Integer tiempoSiguiente = nextSchedule.nextStartsInMinutes() == null
                ? 0
                : nextSchedule.nextStartsInMinutes();

        var tituloProximo = new H2("PROXIMO BLOQUE");
        tituloProximo.addClassName("block-title");

        var descProximo = new H3(siguienteTarea);
        descProximo.addClassName("task-title");

        var resumenProximo = new H3("Descripcion: Descripcion del nuevo entrenamiento");
        resumenProximo.addClassName("next-task-desc");

        var proxTerminaEn = new H3("Empieza en: " + tiempoSiguiente + " MIN");
        proxTerminaEn.addClassName("task-countdown");

        var bloqueProximo = new VerticalLayout(
            tituloProximo,
            descProximo,
            resumenProximo,
            proxTerminaEn
        );
        bloqueProximo.setPadding(false);
        bloqueProximo.addClassNames("focus-block", "focus-block--next");

        return bloqueProximo;
    }

    public Component NoBloquesSiguentesDisponibles() {
        return new BloqueSinTareas();
    }
}