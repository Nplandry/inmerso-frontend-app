package com.InmersoAI.ui.views.settings;

import com.InmersoAI.ui.layouts.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


@Route (value = "Configuración", layout = MainLayout.class)
public class SettingsView extends VerticalLayout {
    public SettingsView() {
        add(new H1("Configuración"));
    }
}