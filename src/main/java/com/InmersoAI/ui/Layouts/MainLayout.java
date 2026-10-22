package com.InmersoAI.ui.layouts;

import com.InmersoAI.ui.session.SesionUsuario;
import com.InmersoAI.ui.views.auth.LoginView;
import com.InmersoAI.ui.views.auth.RegisterView;
import com.InmersoAI.ui.views.focus.AgendarSemana;
import com.InmersoAI.ui.views.focus.FocusView;
import com.InmersoAI.ui.views.settings.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;

public class MainLayout extends AppLayout implements AfterNavigationObserver {

    private final SesionUsuario sesion;

    // Referencia al menu actual para poder removerlo antes de reconstruirlo.
    private SideNav currentNav;

    public MainLayout(SesionUsuario sesion) {
        this.sesion = sesion;
        createHeader();
        // El drawer se construira en afterNavigation()
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

    // Reconstruye el drawer segun el estado de sesion.
    private void refreshDrawer() {
        // Remueve el menu anterior del drawer antes de crear uno nuevo
        // SOL-BUG: para evitar que se apilen varios SideNav en cada navegacion.
        if (currentNav != null) {
            remove(currentNav);
            currentNav = null;
        }

        SideNav nav = new SideNav();

        if (sesion.onboardingCompletado()) {
            nav.addItem(new SideNavItem("Inicio", FocusView.class, VaadinIcon.HOME.create()));
            nav.addItem(new SideNavItem("Agendar Semana", AgendarSemana.class, VaadinIcon.STETHOSCOPE.create()));
            nav.addItem(new SideNavItem("Configuración", SettingsView.class, VaadinIcon.COG.create()));
        } else if (sesion.necesitaOnboarding()) {
            // Usuario nuevo en onboarding: sin menu lateral.
            return;
        } else {
            nav.addItem(new SideNavItem("Logueo", LoginView.class, VaadinIcon.HOME.create()));
            nav.addItem(new SideNavItem("Registro", RegisterView.class, VaadinIcon.HOME.create()));
        }

        currentNav = nav;
        addToDrawer(nav);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        refreshDrawer();
    }
}
