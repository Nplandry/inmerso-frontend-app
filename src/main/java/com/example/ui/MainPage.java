package com.example.ui;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.example.Todo;
import com.example.TodoRepo;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

@Route("")
public class MainPage extends VerticalLayout {
    
    private TodoRepo repo;

    public MainPage(TodoRepo repo){
        this.repo = repo;

        var task = new TextField();
        var button = new Button("new");
        var todos = new VerticalLayout();

        todos.setPadding(false);
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickShortcut(Key.ENTER);

        button.addClickListener(click -> {
            var todo = repo.save(new Todo(task.getValue()));
            todos.add(createCheckbox(todo)); 
            task.clear();
        });

        repo.findAll().forEach(todo -> todos.add(createCheckbox(todo)));

        add(
            new H1("Todo"),
            new HorizontalLayout(task, button),
            todos
        );
        
    }   

    private Component createCheckbox(Todo todo){
        return new Checkbox(todo.getTask(), todo.isDone(), e -> {
            todo.setDone(e.getValue());
            repo.save(todo);
        });
    }

}
