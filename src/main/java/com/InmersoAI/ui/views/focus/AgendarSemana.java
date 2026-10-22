package com.InmersoAI.ui.views.focus;

import com.InmersoAI.ui.layouts.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.DenyAll;

@Route (value = "AgendarSemana", layout = MainLayout.class)
@PageTitle ("AgendarSemana")
@DenyAll 
public class AgendarSemana extends VerticalLayout {



    public AgendarSemana(){
        add(new H1("Agenda toda la semana"));
    }
}
    