package com.InmersoAI.ui.views.auth;

import com.InmersoAI.ui.layouts.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


@Route (value = "Crear Cuenta", layout = MainLayout.class)
public class RegisterView extends VerticalLayout {
    public RegisterView() {
        add(new H1("Crear Cuenta"));
    }
}