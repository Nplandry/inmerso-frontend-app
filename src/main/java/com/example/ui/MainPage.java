package com.example.ui;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.example.Inmerso;
import com.example.InmersoRepo;
import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
//import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//import com.vaadin.flow.component.textfield.TextField;

@Route("")
public class MainPage extends VerticalLayout {
    
    private final InmersoRepo repo; //guardar espacio de puntero
    public MainPage(InmersoRepo repo) {
        this.repo = repo;
        addClassName("main-view");
    
        //var task = new TextField();
        var Listobutton = new Button("Listo");
        Listobutton.addClassNames("action-btn", "action-btn--primary");
        var todosLayout = new VerticalLayout();
        todosLayout.addClassName("todos-container");
        var addTime = new Button("+15 MIN");
        addTime.addClassNames("action-btn", "action-btn--secondary");
        var nuevaTarea = new Button("Agendar nueva tarea por voz");
        nuevaTarea.addClassNames("action-btn", "action-btn--voice");
        var time = new H3("23:22");
        time.addClassName("task-time");
        var TareaReciente = new H2("Revisión de Arquitectura");
        TareaReciente.addClassName("task-title");
        var headerActual = new HorizontalLayout(new H1("HACIENDO AHORA"), time);
        headerActual.addClassName("block-header");
        var terminaEn = new H3("Termina en: 2 min");
        terminaEn.addClassName("task-countdown");
        var bloqueActual = new VerticalLayout(
            headerActual, // Se queda el h1
            TareaReciente,
            terminaEn // Removido el VerticalLayout extra innecesario
        );
        bloqueActual.setPadding(false);
        bloqueActual.addClassNames("focus-block", "focus-block--current");

        var tituloProximo = new H2("PROXIMO BLOQUE");
        tituloProximo.addClassName("block-title");

        var descProximo = new H3("Entrenamiento Gimnasio");
        descProximo.addClassName("next-task-desc");

        var bloqueProximo = new VerticalLayout(
            tituloProximo, // Se queda
            descProximo // Removido el HorizontalLayout extra innecesario
        );
        bloqueProximo.setPadding(false);
        bloqueProximo.addClassNames("focus-block", "focus-block--next");

        var bloqueControles = new HorizontalLayout(addTime, Listobutton);
        bloqueControles.addClassName("controls-container");
        // --- Fin de modularización ---

        todosLayout.setPadding(false);
        //button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Listobutton.addClickShortcut(Key.ENTER);

        nuevaTarea.addClickListener(click -> {
            //var todo = repo.save(new Todo(task.getValue()));
            //todosLayout.add(createCheckbox(todo)); 
            //task.clear();
        });

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
    
    private Component CrearBloque(Inmerso inmerso){
        Checkbox checkbox = new Checkbox(inmerso.getTask(), inmerso.isDone(), e -> {
            inmerso.setDone(e.getValue());
            //inmerso.save(repo);
        });
        checkbox.addClassName("task-checkbox");
        return checkbox;
    }
}