package com.example.ui;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.example.Todo;
import com.example.TodoRepo;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

@Route("")
public class MainPage extends VerticalLayout {
    
    private TodoRepo repo; // Espacio para guardar el puntero

    public MainPage(TodoRepo repo){//entregando la implementacion de jpa
        this.repo = repo; // // Guardas el objeto que Spring fabrica para ti
    

        var task = new TextField();
        var button = new Button("new");
        var todosLayout = new VerticalLayout();

        /**Irrelevante en cuanto a logica del front*/
        todosLayout.setPadding(false);
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickShortcut(Key.ENTER);
        /**FIN| Irrelevante */

        //Al clickear ese nuevo boton....
        button.addClickListener(click -> {
            var todo = repo.save(new Todo(task.getValue()));
            todosLayout.add(createCheckbox(todo)); 
            task.clear();
        });

        repo.findAll().forEach(todo -> todosLayout.add(createCheckbox(todo)));

        add(
            new H1("Todo"),
            new HorizontalLayout(task, button),
            todosLayout
        );
        
    }   

    private Component createCheckbox(Todo todo){
        return new Checkbox(todo.getTask(), todo.isDone(), e -> {
            todo.setDone(e.getValue());
            repo.save(todo);
        });
    }

}
