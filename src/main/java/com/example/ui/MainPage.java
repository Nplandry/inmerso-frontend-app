package com.example.ui;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.example.Inmerso;
import com.example.InmersoRepo;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
//import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
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
    
    private final InmersoRepo repo; //guardar espacio de puntero

    public MainPage(InmersoRepo repo) {
        
        this.repo = repo;
        addClassName("main-view");
    
        var Listobutton = new Button("Listo");
        Listobutton.addClassNames("action-btn", "action-btn--primary");

        var addTime = new Button("+15 MIN");
        addTime.addClassNames("action-btn", "action-btn--secondary");
        
        var nuevaTarea2 = new Button("Nueva Tarea");
        nuevaTarea2.addClassNames("action-btn", "action-btn--voice");

        var nuevaTarea = new Button("Agendar nueva tarea por voz");
        nuevaTarea.addClassNames("action-btn", "action-btn--voice");

        var todosLayout = new VerticalLayout();
        todosLayout.addClassName("todos-container");

        // Contenedor que parte vacío sin elementos embebidos
        var bloqueActual = new VerticalLayout();
        bloqueActual.setPadding(false);

        var tituloProximo = new H2("PROXIMO BLOQUE");
        tituloProximo.addClassName("block-title");

        var descProximo = new H3("Entrenamiento Gimnasio");
        descProximo.addClassName("task-title");

        var resumenProximo = new H3("Descripcion: Descripcion del nuevo entrenamiento");
        resumenProximo.addClassName("next-task-desc");

        var proxTerminaEn = new H3("Termina en: 2 min");
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
            var todo = repo.save(new Inmerso("nueva tarea"));
            bloqueActual.removeAll(); // Limpia cualquier bloque previo para evitar que se embeban
            bloqueActual.add(CrearBloque(todo));
            System.out.println("Funcionando!!!");
        }); 

        nuevaTarea2.addClickListener(click -> {
            abrirModal();
            System.out.println("Funcionando!!!");
        });
        //----[END Listeners]----

        var appTitle = new H1("Inmerso");
        appTitle.addClassName("app-title");

        add(
            appTitle, 
            bloqueActual,
            bloqueProximo,
            bloqueControles,
            nuevaTarea,
            nuevaTarea2
        );

    }   
    /*##LOGICA DEL MODAL */
    public void abrirModal(){
        Dialog dialog = new Dialog();
        dialog.addClassName("modal-container"); 
        dialog.setCloseOnOutsideClick(false);

        dialog.setHeaderTitle("Ingrese su información");
        TextField inputNombre = new TextField("Nombre Completo");
        inputNombre.setPlaceholder("Ej. Juan Pérez");
        VerticalLayout dialogLayout = new VerticalLayout(inputNombre);
        dialog.add(dialogLayout);

        Button botonGuardar = new Button("Guardar", e -> {
            String valorIngresado = inputNombre.getValue();
            dialog.close(); 
        });
        botonGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button botonCancelar = new Button("Cancelar", e -> dialog.close());
        
        dialog.getFooter().add(botonCancelar, botonGuardar);
        dialog.open();
    }
    /*##FIN - LOGICA DEL MODAL */

    private Component CrearBloque(Inmerso inmerso) {
        var time = new H3("23:22");
        time.addClassName("task-time");

        var headerActual = new HorizontalLayout(new H1("HACIENDO AHORA"), time);
        headerActual.addClassName("block-header");

        var msg = new H2(inmerso.getTask());
        msg.addClassName("task-title");

        var terminaEn = new H3("Termina en: 2 min");
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