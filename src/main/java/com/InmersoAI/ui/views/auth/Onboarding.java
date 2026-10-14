package com.InmersoAI.ui.views.auth;

import com.InmersoAI.app.User.CrearUsuarioUseCase;
import com.InmersoAI.domain.model.Usuario;
import com.InmersoAI.ui.layouts.MainLayout;
import com.InmersoAI.ui.views.focus.FocusView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.security.AuthenticationContext;

import jakarta.annotation.security.DenyAll;

@Route(value = "Onboarding", layout = MainLayout.class)
@RouteAlias("onboarding-nuevos-usuarios")
@PageTitle("Bienvenido a InmersoAI")
@DenyAll
public class Onboarding extends VerticalLayout implements BeforeEnterObserver {

    private static final int TOTAL_STEPS = 4;
    private int currentStep = 1;

    private final ProgressBar progressBar = new ProgressBar();
    private final Span stepLabel = new Span();
    private final VerticalLayout contentContainer = new VerticalLayout();

    private final Button btnBack = new Button("Anterior");
    private final Button btnNext = new Button("Siguiente");

    private final TextField nameField = new TextField("¿Cómo prefieres que te llamemos?");
    private final RadioButtonGroup<String> usageType = new RadioButtonGroup<>("¿Cuál es tu objetivo principal?");
    private final ComboBox<String> roleSelect = new ComboBox<>("Tu rol principal");
    private final CrearUsuarioUseCase crearUsuarioUseCase;
    private final AuthenticationContext authenticationContext;
    private Usuario usuarioActual;

    public Onboarding(
            CrearUsuarioUseCase crearUsuarioUseCase,
            AuthenticationContext authenticationContext
    ) {
        this.crearUsuarioUseCase = crearUsuarioUseCase;
        this.authenticationContext = authenticationContext;

        addClassName("onboarding-view");

        progressBar.addClassName("onboarding-progress");
        progressBar.setMin(0.0);
        progressBar.setMax(1.0);
        progressBar.setValue(0.25);

        stepLabel.addClassName("onboarding-step-label");

        contentContainer.addClassName("onboarding-content");

        btnBack.addClassName("onboarding-navigation-button");
        btnBack.setEnabled(false);
        btnBack.addClickListener(e -> navigate(-1));

        btnNext.addClassName("onboarding-navigation-button");
        btnNext.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnNext.addClickListener(e -> navigate(1));
        nameField.addValueChangeListener(e -> updateNextButtonState());
        usageType.addValueChangeListener(e -> updateNextButtonState());
        roleSelect.addValueChangeListener(e -> updateNextButtonState());

        HorizontalLayout navigationLayout = new HorizontalLayout(btnBack, btnNext);
        navigationLayout.addClassName("onboarding-navigation");

        add(progressBar, stepLabel, contentContainer, navigationLayout);

        renderStep();

        boolean existe = crearUsuarioUseCase
        .buscarPorUsername("cliente_prueba")
        .isPresent();

        System.out.println("¿Existe cliente_prueba?: " + existe);
    }


    private void navigate(int delta) {
        if (delta > 0 && !isCurrentStepValid()) {
            return;
        }
        currentStep += delta;
        if (currentStep < 1) {
            currentStep = 1;
        }
        if (currentStep > TOTAL_STEPS) {
            currentStep = TOTAL_STEPS;
        }
        renderStep();
    }

    private void renderStep() {
        contentContainer.removeAll();
        progressBar.setValue((double) currentStep / TOTAL_STEPS);
        stepLabel.setText("Paso " + currentStep + " de " + TOTAL_STEPS);

        btnBack.setVisible(currentStep > 1);
        btnBack.setEnabled(currentStep > 1);
        btnNext.setVisible(currentStep < TOTAL_STEPS);
        updateNextButtonState();

        switch (currentStep) {
            case 1 -> contentContainer.add(createStepOne());
            case 2 -> contentContainer.add(createStepTwo());
            case 3 -> contentContainer.add(createStepThree());
            case 4 -> contentContainer.add(createStepFourPlans());
            default -> {
            }
        }
    }

    private Component createStepOne() {
        VerticalLayout step = new VerticalLayout();
        step.addClassNames("onboarding-step", "onboarding-step--centered");

        H1 title = new H1("¡Bienvenido a InmersoAI!");
        Paragraph description = new Paragraph("Configuremos tu cuenta en solo un par de minutos para personalizar tu experiencia.");

        nameField.setPlaceholder("Tu nombre o apodo");
        nameField.addClassName("onboarding-field");
        nameField.setRequiredIndicatorVisible(true);

        step.add(title, description, nameField);
        btnNext.setText("Siguiente");
        return step;
    }

    private Component createStepTwo() {
        VerticalLayout step = new VerticalLayout();
        step.addClassNames("onboarding-step", "onboarding-step--full");

        H2 title = new H2("¿Para qué usas InmersoAI?");

        usageType.setItems("Proyectos Personales", "Trabajo Profesional / Freelance", "Estudios / Investigación", "Empresa / Equipo");
        usageType.addClassName("onboarding-field");
        usageType.setRequiredIndicatorVisible(true);

        roleSelect.setItems("Desarrollador", "Diseñador", "Product Manager", "Estudiante", "Otro");
        roleSelect.setPlaceholder("Selecciona una opción");
        roleSelect.addClassName("onboarding-field");
        roleSelect.setRequiredIndicatorVisible(true);

        step.add(title, usageType, roleSelect);
        btnNext.setText("Siguiente");
        return step;
    }

    private Component createStepThree() {
        VerticalLayout step = new VerticalLayout();
        step.addClassNames("onboarding-step", "onboarding-step--centered");

        H2 title = new H2("¡Todo casi listo!");
        Paragraph text = new Paragraph("Hemos adaptado tu entorno de trabajo según tus objetivos. Haz clic en continuar para explorar los planes y activar tu cuenta.");

        Icon icon = VaadinIcon.CHECK_CIRCLE.create();
        icon.addClassName("onboarding-success-icon");

        step.add(icon, title, text);
        btnNext.setText("Ver Planes");
        return step;
    }

    private Component createStepFourPlans() {
        VerticalLayout step = new VerticalLayout();
        step.addClassNames("onboarding-step", "onboarding-step--centered");

        H2 title = new H2("Elige el plan ideal para ti");
        Paragraph subtitle = new Paragraph("Comienza gratis o desbloquea todo el potencial con nuestras opciones Pro.");

        HorizontalLayout plansLayout = new HorizontalLayout();
        plansLayout.addClassName("onboarding-plans");

        VerticalLayout freePlan = buildPlanCard("Free", "$0 / mes", "Ideal para explorar", "Acceso a funcionalidades básicas", "Comenzar Gratis", false, () -> finishOnboarding("Free"));
        VerticalLayout proPlan = buildPlanCard("Pro", "$19 / mes", "Para usuarios avanzados", "Modelos IA sin límites y funcionalidades unicas", "Probar Pro 14 Días", true, () -> finishOnboarding("Pro"));

        plansLayout.add(freePlan, proPlan);
        step.add(title, subtitle, plansLayout);
        btnNext.setVisible(false);
        return step;
    }

    private VerticalLayout buildPlanCard(String name, String price, String desc, String feature, String buttonText, boolean highlighted, Runnable onSelect) {
        VerticalLayout card = new VerticalLayout();
        card.addClassName("onboarding-plan-card");
        if (highlighted) {
            card.addClassName("onboarding-plan-card--highlighted");
        }

        H3 planName = new H3(name);
        Span planPrice = new Span(price);
        planPrice.addClassName("onboarding-plan-price");

        Paragraph planDesc = new Paragraph(desc);
        planDesc.addClassName("onboarding-plan-description");

        Paragraph planFeature = new Paragraph("• " + feature);
        planFeature.addClassName("onboarding-plan-feature");

        Button selectBtn = new Button(buttonText, e -> onSelect.run());
        if (highlighted) {
            selectBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        }

        card.add(planName, planPrice, planDesc, planFeature, selectBtn);
        return card;
    }

    private void finishOnboarding(String planSelected) {
        if (usuarioActual != null && !usuarioActual.isOnboardingCompletado()) {
            crearUsuarioUseCase.completarOnboarding(usuarioActual.getUsername());
        }
        getUI().ifPresent(ui -> ui.navigate(""));
    }

    private void updateNextButtonState() {
        btnNext.setEnabled(isCurrentStepValid());
    }

    private boolean isCurrentStepValid() {
        return switch (currentStep) {
            case 1 -> nameField.getValue() != null && !nameField.getValue().isBlank();
            case 2 -> usageType.getValue() != null && roleSelect.getValue() != null;
            default -> true;
        };
    }


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        //boolean usuarioAutenticado = comprobarSesion();

        if (true) {
            event.forwardTo(FocusView.class);
        } else if(false) {
            event.forwardTo(Onboarding.class);
        }
    }
}