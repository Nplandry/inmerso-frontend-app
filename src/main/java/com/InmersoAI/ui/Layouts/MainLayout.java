package com.InmersoAI.ui.layouts;

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

    //instanciar objeto, clase autentificaion...

    public MainLayout() {
        //if (authContext.isAuthenticated())
        if (true) {
            createHeader();
            createDrawer();
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

        if(false) {
            nav.addItem(new SideNavItem("Inicio", FocusView.class, VaadinIcon.HOME.create()));
            nav.addItem(new SideNavItem("Configuración", SettingsView.class, VaadinIcon.COG.create()));
        } else {
            nav.addItem(new SideNavItem("Logueo", LoginView.class, VaadinIcon.HOME.create()));
            nav.addItem(new SideNavItem("Registro", RegisterView.class, VaadinIcon.HOME.create()));
        }

        addToDrawer(nav);
    }
}
