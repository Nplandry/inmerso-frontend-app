package com.example.ui;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.example.app.TareaAppService;
import com.example.domain.Tarea;
import com.example.infraestructure.external.dto.ScheduleMapper;
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

    private VerticalLayout bloqueActual; // Convertir a atributo de la clase
    private String valorIngresado;
    private Tarea tareaActual;
    private Button Listobutton; 
    private Button addTime; 


    public MainPage(TareaAppService tareaService, ScheduleMapper scheduleMapper) {
        this.tareaService = tareaService;
        this.scheduleMapper = scheduleMapper;
        addClassName("main-view");
    
        Listobutton = new Button("Listo");
        Listobutton.addClassNames("action-btn", "action-btn--primary");
        Listobutton.setEnabled(false);
        Listobutton.addClickShortcut(Key.ENTER);
        addTime = new Button("+15 MIN");
        addTime.addClassNames("action-btn", "action-btn--secondary");
        addTime.setEnabled(false);


        /*###IGNORAR: DEF VARIABLES ESTATICOS ###*/
        var appTitle = new H1("Inmerso");
        appTitle.addClassName("app-title");
        var nuevaTarea = new Button("Nueva Tarea");
        nuevaTarea.addClassNames("action-btn", "action-btn--voice");
        var todosLayout = new VerticalLayout();
        todosLayout.addClassName("todos-container");
        bloqueActual = new VerticalLayout(); // Contenedor que parte vacío sin elementos embebidos, ya definido, encapsulado y persistente.
        bloqueActual.setPadding(false);
        var bloqueControles = new HorizontalLayout(addTime, Listobutton);
        bloqueControles.addClassName("controls-container");
        /*###FIN IGNORAR: DEF VARIABLES ESTATICOS ###*/



        //#######--[Listeners]----#######
        nuevaTarea.addClickListener(click -> {
            abrirModal();
        });

        Listobutton.addClickListener(click -> {
            if (tareaActual != null) {
            tareaService.completarTarea(tareaActual.getId())
                    .ifPresent(tarea -> tareaActual = tarea);
            bloqueActual.removeAll();

            var completado = new VerticalLayout(
                new H1("COMPLETADO"),
                new H2(tareaActual.getDescripcion())
            );
            completado.addClassName("focus-block--completed");
            bloqueActual.add(completado);
            Listobutton.setEnabled(false);
            addTime.setEnabled(false);
        }
        });
        addTime.addClickListener(click -> {
        // Asume que tienes una referencia a la tarea actual
        if (tareaActual != null) {
            tareaService.agregarTiempo(tareaActual.getId(), 15)
                    .ifPresent(tarea -> tareaActual = tarea);
            bloqueActual.removeAll();
            bloqueActual.add(CrearBloque(tareaActual));
        }
    });
    //#######----[END Listeners]----#######

    add(appTitle, bloqueActual, CrearProximoBloque(), bloqueControles, nuevaTarea);}

    /*######--LOGICA DEL MODAL--######*/
    public void abrirModal(){
        Dialog dialog = new Dialog();
           dialog.addClassName("modal-container"); 
        dialog.setCloseOnOutsideClick(false);

        dialog.setHeaderTitle("Ingrese su información");
        TextField inputNombre = new TextField("Descripcion Tarea");
        inputNombre.setPlaceholder("Estudiar en una hora mas");
        VerticalLayout dialogLayout = new VerticalLayout(inputNombre);
        dialog.add(dialogLayout);
        
        Button botonGuardar = new Button("Guardar", e -> {
            valorIngresado = inputNombre.getValue(); //TODO: controlar valor nulo
            System.out.println(valorIngresado); // Valor input!!
            dialog.close(); 
        });

        botonGuardar.addClickShortcut(Key.ENTER);
        botonGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        botonGuardar.addClickListener((e) -> {
        var todo = tareaService.crearTarea(valorIngresado);
        //todo: Componente en memoria mientras se hace la peticion al backend 
        tareaActual = todo; 
        Listobutton.setEnabled(true);  // Habilitar
        addTime.setEnabled(true);    
        bloqueActual.removeAll();
        bloqueActual.add(CrearBloque(tareaActual));
        });

        Button botonCancelar = new Button("Cancelar", e -> dialog.close());
        
        dialog.getFooter().add(botonCancelar, botonGuardar);
        dialog.open();
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
                Listobutton.setEnabled(true);
                addTime.setEnabled(true);
                bloqueActual.add(CrearBloque(tareaActual));
            }
        }
    }

    private Component CrearProximoBloque() {
        var response = scheduleMapper.obtenerProximoBloque();

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
    var bloqueVacio = new VerticalLayout(
        new H1("SIN TAREAS"),
        new H2("No hay tareas para despues")
    );
        bloqueVacio.addClassName("focus-block--completed");
        bloqueVacio.addClassName("focus-unavailable-task");
        return bloqueVacio;
    }

    private Component CrearBloque(Tarea tarea) {
        var time = new H3(tarea.getHoraFormato()); 
        time.addClassName("task-time");

        var headerActual = new HorizontalLayout(new H1("HACIENDO AHORA"), time);
        headerActual.addClassName("block-header");

        var msg = new H2(tarea.getDescripcion());
        msg.addClassName("task-title");

        var response = scheduleMapper.obtenerScheduleCompleto();
        var data = response != null ? response.data() : null;
        Integer tiempoSiguiente = (data != null && data.time_remaining() != null
                && data.time_remaining().current_ends_in_minutes() != null)
                ? data.time_remaining().current_ends_in_minutes()
                : 0;
            

        var terminaEn = new H3("Termina en: " + tiempoSiguiente + " MIN");
        terminaEn.addClassName("task-countdown");

        var descActual = new H3("Descripcion: Descripcion de la nueva tarea");
        descActual.addClassName("next-task-desc");

        var nuevoBloqueEntero = new VerticalLayout(
            headerActual,
            msg,
            descActual,
            terminaEn

        );
        nuevoBloqueEntero.setPadding(false);
        nuevoBloqueEntero.addClassNames("focus-block", "focus-block--current");

        return nuevoBloqueEntero;
    }
}