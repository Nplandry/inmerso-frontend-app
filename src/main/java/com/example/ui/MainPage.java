package com.example.ui;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.example.app.TareaAppService;
import com.example.controller.ApiController;
import com.example.domain.Tarea;
import com.example.infraestructure.external.dto.ScheduleMapper;
import com.example.infraestructure.external.dto.ScheduleResponse;
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
    private final ScheduleMapper scheduleMapper;
    private  final ApiController apiController;

    private final VerticalLayout bloqueActual = new VerticalLayout();
    private Tarea tareaActual;
    private Integer tiempoActual;
    private final Button listoButton;
    private final Button agregarTiempoButton;


    public MainPage(TareaAppService tareaService, ScheduleMapper scheduleMapper, ApiController apiController) {
        this.tareaService = tareaService;
        this.scheduleMapper = scheduleMapper;
        this.apiController = apiController;
        addClassName("main-view");
    
        listoButton = new Button("Listo");
        listoButton.addClassNames("action-btn", "action-btn--primary");
        listoButton.setEnabled(false);
        listoButton.addClickShortcut(Key.ENTER);

        agregarTiempoButton = new Button("+15 MIN");
        agregarTiempoButton.addClassNames("action-btn", "action-btn--secondary");
        agregarTiempoButton.setEnabled(false);


        var respondeService = apiController.obtenerTodas();
        System.out.println("AHI VAAA: " + respondeService);


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
        var response = scheduleMapper.obtenerScheduleCompleto();

        if (response != null && response.data() != null) {
            var data = response.data();
            
            if (data.current_task() != null) {
                var tarea = data.current_task().title();
                tareaActual = tareaService.crearTarea(tarea);
                tiempoActual = obtenerTiempoActual(data);
                actualizarBloqueActual();
            }
        }
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

    private int obtenerTiempoActual(ScheduleResponse.ScheduleData data) {
        return data.time_remaining() != null
                && data.time_remaining().current_ends_in_minutes() != null
                ? data.time_remaining().current_ends_in_minutes()
                : 0;
    }

    private Component crearBloqueProximo() {
        var response = scheduleMapper.obtenerBloque();

        if (response == null || response.data() == null || response.data().next_task() == null) {
            return NoBloquesSiguentesDisponibles();
        }

        var nextTask = response.data().next_task();
        var timeRemaining = response.data().time_remaining();

        String siguienteTarea = (nextTask.title() != null && !nextTask.title().isBlank())
            ? nextTask.title()
            : "Tarea Personalizada";

        Integer tiempoSiguiente = (timeRemaining != null && timeRemaining.next_starts_in_minutes() != null)
            ? timeRemaining.next_starts_in_minutes()
            : 0;

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

    /* El bloque actual usa la tarea de dominio, no vuelve a consultar el backend. */
}