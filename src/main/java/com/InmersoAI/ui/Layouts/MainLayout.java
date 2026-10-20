package com.InmersoAI.ui.layouts;

import com.InmersoAI.ui.session.SesionUsuario;
import com.InmersoAI.ui.views.auth.LoginView;
import com.InmersoAI.ui.views.auth.RegisterView;
import com.InmersoAI.ui.views.focus.FocusView;
import com.InmersoAI.ui.views.settings.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
public class MainLayout extends AppLayout {

    private final SesionUsuario sesion;

    public MainLayout(SesionUsuario sesion) {
        this.sesion = sesion;

        if (sesion.estaAutenticado()) {
            createHeader();
            createDrawer();
        } else if (false){
            System.out.println("Usuario Nuevo");
        }
    }

    private void createHeader() {
        H1 logoTitle = new H1("INMERSO");
        logoTitle.addClassName("app-title");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logoTitle);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.addClassNames("py-0", "px-xl");

        addToNavbar(header);
    }

    private void createDrawer() {
        SideNav nav = new SideNav();

        if (sesion.onboardingCompletado()) {
            nav.addItem(new SideNavItem("Inicio", FocusView.class, VaadinIcon.HOME.create()));
            nav.addItem(new SideNavItem("Configuración", SettingsView.class, VaadinIcon.COG.create()));
        } else if(sesion.necesitaOnboarding()){
            System.out.println("Usuario Nuevo");
        } else {
            nav.addItem(new SideNavItem("Logueo", LoginView.class, VaadinIcon.HOME.create()));
            nav.addItem(new SideNavItem("Registro", RegisterView.class, VaadinIcon.HOME.create()));
        }

        addToDrawer(nav);
    }
}
