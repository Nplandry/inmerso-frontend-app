package com.InmersoAI.ui.views.auth;

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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route("Onboarding")
@RouteAlias("onboarding-nuevos-usuarios")
@PageTitle("Bienvenido a InmersoAI")
public class Onboarding extends VerticalLayout {

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

    public Onboarding() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        setAlignItems(Alignment.STRETCH);
        getStyle().set("background-color", "var(--lumo-contrast-5pct)");

        progressBar.setMin(0.0);
        progressBar.setMax(1.0);
        progressBar.setValue(0.25);
        progressBar.setHeight("6px");
        progressBar.setWidthFull();
        progressBar.getStyle().set("flex-shrink", "0");

        stepLabel.getStyle()
                .set("align-self", "flex-end")
                .set("padding", "0.75rem 5%");

        contentContainer.setWidth("92%");
        contentContainer.setMaxWidth("560px");
        contentContainer.setPadding(true);
        contentContainer.setSpacing(true);
        contentContainer.setAlignItems(Alignment.CENTER);
        contentContainer.setJustifyContentMode(JustifyContentMode.CENTER);
        contentContainer.getStyle()
                .set("align-self", "center")
                .set("box-sizing", "border-box")
                .set("border", "1px solid var(--lumo-contrast-10pct)")
                .set("border-radius", "28px")
                .set("background-color", "var(--lumo-base-color)")
                .set("box-shadow", "0 12px 32px var(--lumo-contrast-10pct)");

        btnBack.setEnabled(false);
        btnBack.addClickListener(e -> navigate(-1));
        btnBack.setWidth("42%");
        btnBack.setHeight("52px");
        btnBack.getStyle().set("border-radius", "999px");

        btnNext.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnNext.addClickListener(e -> navigate(1));
        btnNext.setWidth("42%");
        btnNext.setHeight("52px");
        btnNext.getStyle().set("border-radius", "999px");

        HorizontalLayout navigationLayout = new HorizontalLayout(btnBack, btnNext);
        navigationLayout.setWidth("92%");
        navigationLayout.setMaxWidth("560px");
        navigationLayout.setAlignItems(Alignment.CENTER);
        navigationLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        navigationLayout.setPadding(false);
        navigationLayout.getStyle()
                .set("margin-top", "auto")
                .set("padding", "1.25rem 0 2rem")
                .set("align-self", "center")
                .set("box-sizing", "border-box");

        add(progressBar, stepLabel, contentContainer, navigationLayout);
        setFlexGrow(1, contentContainer);

        renderStep();
    }

    private void navigate(int delta) {
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
        step.setAlignItems(Alignment.CENTER);
        step.setPadding(false);
        step.setSpacing(true);

        H1 title = new H1("¡Bienvenido a InmersoAI!");
        Paragraph description = new Paragraph("Configuremos tu cuenta en solo un par de minutos para personalizar tu experiencia.");

        nameField.setPlaceholder("Tu nombre o apodo");
        nameField.setWidthFull();

        step.add(title, description, nameField);
        btnNext.setText("Siguiente");
        return step;
    }

    private Component createStepTwo() {
        VerticalLayout step = new VerticalLayout();
        step.setAlignItems(Alignment.START);
        step.setPadding(false);
        step.setSpacing(true);
        step.setWidthFull();

        H2 title = new H2("¿Para qué usas InmersoAI?");

        usageType.setItems("Proyectos Personales", "Trabajo Profesional / Freelance", "Estudios / Investigación", "Empresa / Equipo");
        usageType.setValue("Proyectos Personales");
        usageType.setWidthFull();

        roleSelect.setItems("Desarrollador", "Diseñador", "Product Manager", "Estudiante", "Otro");
        roleSelect.setPlaceholder("Selecciona una opción");
        roleSelect.setWidthFull();

        step.add(title, usageType, roleSelect);
        btnNext.setText("Siguiente");
        return step;
    }

    private Component createStepThree() {
        VerticalLayout step = new VerticalLayout();
        step.setAlignItems(Alignment.CENTER);
        step.setPadding(false);
        step.setSpacing(true);

        H2 title = new H2("¡Todo casi listo!");
        Paragraph text = new Paragraph("Hemos adaptado tu entorno de trabajo según tus objetivos. Haz clic en continuar para explorar los planes y activar tu cuenta.");

        Icon icon = VaadinIcon.CHECK_CIRCLE.create();
        icon.getStyle()
                .set("font-size", "64px")
                .set("color", "var(--lumo-success-color)");

        step.add(icon, title, text);
        btnNext.setText("Ver Planes");
        return step;
    }

    private Component createStepFourPlans() {
        VerticalLayout step = new VerticalLayout();
        step.setAlignItems(Alignment.CENTER);
        step.setPadding(false);
        step.setSpacing(true);

        H2 title = new H2("Elige el plan ideal para ti");
        Paragraph subtitle = new Paragraph("Comienza gratis o desbloquea todo el potencial con nuestras opciones Pro.");

        HorizontalLayout plansLayout = new HorizontalLayout();
        plansLayout.setWidthFull();
        plansLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        plansLayout.setSpacing(true);
        plansLayout.getStyle().set("flex-wrap", "wrap");

        VerticalLayout freePlan = buildPlanCard("Free", "$0 / mes", "Ideal para explorar", "Acceso a funcionalidades básicas", "Comenzar Gratis", false, () -> finishOnboarding("Free"));
        VerticalLayout proPlan = buildPlanCard("Pro", "$19 / mes", "Para usuarios avanzados", "Modelos IA sin límites + Soporte prioritario", "Probar Pro 14 Días", true, () -> finishOnboarding("Pro"));

        plansLayout.add(freePlan, proPlan);
        step.add(title, subtitle, plansLayout);
        btnNext.setVisible(false);
        return step;
    }

    private VerticalLayout buildPlanCard(String name, String price, String desc, String feature, String buttonText, boolean highlighted, Runnable onSelect) {
        VerticalLayout card = new VerticalLayout();
        card.setWidth("240px");
        card.setPadding(true);
        card.setSpacing(true);
        card.setAlignItems(Alignment.CENTER);
        card.getStyle()
                .set("border", highlighted ? "1px solid var(--lumo-primary-color)" : "1px solid var(--lumo-contrast-20pct)")
                .set("border-radius", "20px")
                .set("background-color", highlighted ? "var(--lumo-primary-color-10pct)" : "var(--lumo-base-color)")
                .set("padding", "1.25rem")
                .set("box-sizing", "border-box");

        H3 planName = new H3(name);
        Span planPrice = new Span(price);
        planPrice.getStyle().set("font-weight", "600").set("font-size", "1.2rem");

        Paragraph planDesc = new Paragraph(desc);
        planDesc.getStyle().set("font-size", "0.85rem").set("text-align", "center");

        Paragraph planFeature = new Paragraph("• " + feature);
        planFeature.getStyle().set("font-size", "0.8rem").set("text-align", "center");

        Button selectBtn = new Button(buttonText, e -> onSelect.run());
        if (highlighted) {
            selectBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        }

        card.add(planName, planPrice, planDesc, planFeature, selectBtn);
        return card;
    }

    private void finishOnboarding(String planSelected) {
        getUI().ifPresent(ui -> ui.navigate(""));
    }
}