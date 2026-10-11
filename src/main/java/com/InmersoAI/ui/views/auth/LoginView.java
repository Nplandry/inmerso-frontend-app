package com.InmersoAI.ui.views.auth;

import com.InmersoAI.ui.layouts.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


@Route (value = "Inicio de Sesion", layout = MainLayout.class)
public class LoginView extends VerticalLayout {
    public LoginView() {
        add(new H1("Inicio de Sesion"));
    }
}