package com.InmersoAI.ui.views.auth;
import com.InmersoAI.ui.layouts.MainLayout;
import com.InmersoAI.ui.views.focus.FocusView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "Login", layout = MainLayout.class)
@RouteAlias("inicio-sesion") 
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    public LoginView() {
        var userInput = new Input();
        var passwordInput = new Input();
        var submitBtn = new Button("Entrar");
        var loginForm = new VerticalLayout(userInput, passwordInput, submitBtn);

        add(new VerticalLayout(new H1("Inicio de Sesión"), loginForm));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        //boolean usuarioAutenticado = comprobarSesion();

        if (true) {
            event.forwardTo(FocusView.class);
        } else if(false) {
            event.forwardTo(Onboarding.class);
        } else {
            event.forwardTo(LoginView.class);
        }
    }

    /*private boolean comprobarSesion() {
        return false; 
    }*/
}