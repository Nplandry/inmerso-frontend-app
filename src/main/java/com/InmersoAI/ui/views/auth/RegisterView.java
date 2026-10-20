package com.InmersoAI.ui.views.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.InmersoAI.app.User.CrearUsuarioUseCase;
import com.InmersoAI.ui.layouts.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "Crear-Cuenta", layout = MainLayout.class)
@RouteAlias("registro")
public class RegisterView extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(RegisterView.class);

    private final CrearUsuarioUseCase crearUsuarioUseCase;

    private final TextField username = new TextField("Usuario");
    private final PasswordField password = new PasswordField("Contraseña");
    private final PasswordField confirmPassword = new PasswordField("Confirmar contraseña");

    public RegisterView(CrearUsuarioUseCase crearUsuarioUseCase) {
        this.crearUsuarioUseCase = crearUsuarioUseCase;
        log.info("[RegisterView] Construyendo vista de registro.");

        addClassName("auth-view");
        setSizeFull();
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setAlignItems(FlexComponent.Alignment.CENTER);

        add(crearTarjeta());
    }

    private VerticalLayout crearTarjeta() {
        VerticalLayout card = new VerticalLayout();
        card.addClassName("auth-card");
        card.setPadding(true);
        card.setSpacing(true);
        card.setAlignItems(FlexComponent.Alignment.STRETCH);

        H2 titulo = new H2("Crear cuenta");
        titulo.addClassName("auth-card__title");

        Paragraph subtitulo = new Paragraph("Regístrate para empezar a usar InmersoAI");
        subtitulo.addClassName("auth-card__subtitle");

        username.addClassName("auth-field");
        username.setRequiredIndicatorVisible(true);
        password.addClassName("auth-field");
        password.setRequiredIndicatorVisible(true);
        confirmPassword.addClassName("auth-field");
        confirmPassword.setRequiredIndicatorVisible(true);

        Button registrar = new Button("Registrarse", e -> registrar());
        registrar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        registrar.addClassName("auth-submit");
        registrar.addClickShortcut(Key.ENTER);

        card.add(titulo, subtitulo, username, password, confirmPassword, registrar);
        return card;
    }

    private void registrar() {
        String user = username.getValue() == null ? "" : username.getValue().trim();
        String pass = password.getValue() == null ? "" : password.getValue();
        String confirm = confirmPassword.getValue() == null ? "" : confirmPassword.getValue();

        if (user.isBlank() || pass.isBlank()) {
            log.warn("[RegisterView] Registro rechazado: campos obligatorios vacios.");
            notificarError("Usuario y contraseña son obligatorios.");
            return;
        }
        if (!pass.equals(confirm)) {
            log.warn("[RegisterView] Registro rechazado: las contrasenas no coinciden (usuario='{}').", user);
            notificarError("Las contraseñas no coinciden.");
            return;
        }

        try {
            crearUsuarioUseCase.crearUsuario(user, pass);
            log.info("[RegisterView] Usuario '{}' registrado correctamente -> redirigiendo a Login.", user);
            Notification ok = Notification.show("Cuenta creada. Ya puedes iniciar sesión.");
            ok.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            getUI().ifPresent(ui -> ui.navigate(LoginView.class));
        } catch (IllegalArgumentException ex) {
            log.warn("[RegisterView] Error al registrar usuario '{}': {}", user, ex.getMessage());
            notificarError(ex.getMessage());
        }
    }

    private void notificarError(String mensaje) {
        Notification n = Notification.show(mensaje);
        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}
