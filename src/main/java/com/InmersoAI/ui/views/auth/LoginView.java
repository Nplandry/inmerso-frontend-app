package com.InmersoAI.ui.views.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.InmersoAI.ui.layouts.MainLayout;
import com.InmersoAI.ui.session.SesionUsuario;
import com.InmersoAI.ui.views.focus.FocusView;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "Login", layout = MainLayout.class)
@RouteAlias("inicio-sesion")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private static final Logger log = LoggerFactory.getLogger(LoginView.class);

    private final SesionUsuario sesion;
    private final LoginForm loginForm = new LoginForm();

    public LoginView(SesionUsuario sesion) {
        this.sesion = sesion;
        log.info("[LoginView] Construyendo vista de login. Autenticado={}", sesion.estaAutenticado());

        addClassName("login-view");
        setSizeFull();
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setAlignItems(FlexComponent.Alignment.CENTER);

        // Envia el POST de las credenciales a Spring Security 
        loginForm.setAction("login");
        loginForm.setForgotPasswordButtonVisible(false);

        add(loginForm);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        boolean hayError = event.getLocation().getQueryParameters()
                .getParameters().containsKey("error");

        if (hayError) {
            log.warn("[LoginView] Login fallido: credenciales incorrectas (parametro ?error presente).");
            loginForm.setError(true);
            return;
        }

        if (sesion.onboardingCompletado()) {
            log.info("[LoginView] Usuario '{}' autenticado con onboarding COMPLETADO -> FocusView.",
                    sesion.usernameAutenticado().orElse("?"));
            event.forwardTo(FocusView.class);
        } else if (sesion.necesitaOnboarding()) {
            log.info("[LoginView] Usuario '{}' autenticado con onboarding PENDIENTE -> Onboarding.",
                    sesion.usernameAutenticado().orElse("?"));
            event.forwardTo(Onboarding.class);
        } else {
            log.info("[LoginView] Sin usuario autenticado -> se muestra el formulario de login.");
        }
    }
}
