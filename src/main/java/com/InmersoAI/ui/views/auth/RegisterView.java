package com.InmersoAI.ui.views.auth;

import com.InmersoAI.ui.layouts.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


@Route (value = "Crear-Cuenta", layout = MainLayout.class)
public class RegisterView extends VerticalLayout {
    public RegisterView() {
        var UserInput = new Input();
        var PasswordInput = new Input();
        var submitBtn = new Button("Entrar");
        var RegisterForm = new VerticalLayout(UserInput, PasswordInput, submitBtn);
        add(new VerticalLayout(new H1("Registrate"), RegisterForm));
    }
}