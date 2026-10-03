package com.example.ui;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.example.Inmerso;
import com.example.InmersoRepo;
import com.example.services.ApiService;
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
    
    private final InmersoRepo repo;
    private final ApiService apiService;

    private VerticalLayout bloqueActual; // Convertir a atributo de la clase
    private String valorIngresado;
    private Inmerso tareaActual;
    private Button Listobutton; 
    private Button addTime; 


    public MainPage(InmersoRepo repo, ApiService apiService) {
        this.repo = repo;
        this.apiService = apiService;

        addClassName("main-view");
    
        Listobutton = new Button("Listo");
        Listobutton.addClassNames("action-btn", "action-btn--primary");

        Listobutton.setEnabled(false);


        addTime = new Button("+15 MIN");
        addTime.addClassNames("action-btn", "action-btn--secondary");
        
        addTime.setEnabled(false);

        var nuevaTarea = new Button("Nueva Tarea");
        nuevaTarea.addClassNames("action-btn", "action-btn--voice");

        var todosLayout = new VerticalLayout();
        todosLayout.addClassName("todos-container");

        // Contenedor que parte vacío sin elementos embebidos, ya definido, encapsulado y persistente.
        bloqueActual = new VerticalLayout();
        bloqueActual.setPadding(false);

        var tituloProximo = new H2("PROXIMO BLOQUE");
        tituloProximo.addClassName("block-title");

        String siguienteTarea = apiService.obtenerSiguienteTarea();
        var descProximo = new H3(siguienteTarea != null ? siguienteTarea : "Tarea Personalizada");
        descProximo.addClassName("task-title");

        var resumenProximo = new H3("Descripcion: Descripcion del nuevo entrenamiento");
        resumenProximo.addClassName("next-task-desc");

        var proxTerminaEn = new H3("Empieza en: " + apiService.obtenerTiempoSiguente() + " MIN");
        proxTerminaEn.addClassName("task-countdown");

        var bloqueProximo = new VerticalLayout(
            tituloProximo, 
            descProximo, 
            resumenProximo,
            proxTerminaEn
        );

        bloqueProximo.setPadding(false);
        bloqueProximo.addClassNames("focus-block", "focus-block--next");

        var bloqueControles = new HorizontalLayout(addTime, Listobutton);
        bloqueControles.addClassName("controls-container");

        //button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Listobutton.addClickShortcut(Key.ENTER);

        //----[Listeners]----
        nuevaTarea.addClickListener(click -> {
            abrirModal();
        });


        Listobutton.addClickListener(click -> {
            if (tareaActual != null) {
            tareaActual.setDone(true);
            repo.save(tareaActual);
            bloqueActual.removeAll();

            var completado = new VerticalLayout(
                new H1("COMPLETADO"),
                new H2(tareaActual.getTask())
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
            tareaActual.sumarTiempoRestante();
            repo.save(tareaActual); // Persiste los cambios
            bloqueActual.removeAll();
            bloqueActual.add(CrearBloque(tareaActual));
        }
    });
//----[END Listeners]----

        var appTitle = new H1("Inmerso");
        appTitle.addClassName("app-title");

        add(
            appTitle, 
            bloqueActual,
            bloqueProximo,
            bloqueControles,
            nuevaTarea
        );

    }
    /*##LOGICA DEL MODAL */
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
        var todo = repo.save(new Inmerso(valorIngresado)); //Falta validarlo
        //todo: Componente en memoria mientras se hace la peticion al backend 
        tareaActual = todo; 
        Listobutton.setEnabled(true);  // Habilitar
        addTime.setEnabled(true);    
        bloqueActual.removeAll();
        bloqueActual.add(CrearBloque(todo));
        });

        Button botonCancelar = new Button("Cancelar", e -> dialog.close());
        
        dialog.getFooter().add(botonCancelar, botonGuardar);
        dialog.open();
    }

    //###Logica backend
    @PostConstruct
    public void cargarTareaDelBackend(){
        var tarea = apiService.obtenerTareaActual();
        //var hora = apiService.obtenerTiempoRestante();
        //System.out.println(hora);
        if(tarea != null){
            var todo = repo.save(new Inmerso(tarea)); 
            bloqueActual.add(CrearBloque(todo));
        }
    } 


    /*##FIN - LOGICA DEL MODAL */
    private Component CrearBloque(Inmerso inmerso) {
        var time = new H3(inmerso.getHoraFormato()); 
        time.addClassName("task-time");

        var headerActual = new HorizontalLayout(new H1("HACIENDO AHORA"), time);
        headerActual.addClassName("block-header");

        var msg = new H2(inmerso.getTask());
        msg.addClassName("task-title");

        var terminaEn = new H3("Termina en: " + inmerso.getTiempoRestante() + " MIN");
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