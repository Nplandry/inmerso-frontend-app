package com.InmersoAI.ui.views.focus;

import com.InmersoAI.ui.layouts.MainLayout;
import com.InmersoAI.ui.session.SesionUsuario;
import com.InmersoAI.ui.views.auth.LoginView;
import com.InmersoAI.ui.views.auth.Onboarding;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.DenyAll;

@Route(value = "AgendarSemana", layout = MainLayout.class)
@PageTitle("AgendarSemana")
@DenyAll
public class AgendarSemana extends VerticalLayout implements BeforeEnterObserver {

    private final SesionUsuario sesion;

    private final VerticalLayout calendarSection = new VerticalLayout();
    private final HorizontalLayout calendarRow = new HorizontalLayout();
    private final VerticalLayout searchMessages = new VerticalLayout();
    private final Input searchInput = new Input();
    private final Button searchButton = new Button("Buscar");
    private final Button voiceButton = new Button("Voz");
    private final HorizontalLayout searchBar = new HorizontalLayout();
    private boolean calendarsExpanded;

    public AgendarSemana(SesionUsuario sesion) {
        this.sesion = sesion;
        addClassNames("main-view", "calendar-page");
        setPadding(false);

        H1 title = new H1("Agenda toda la semana");
        title.addClassName("calendar-title");

        Span description = new Span("Organiza tus bloques de enfoque de un vistazo");
        description.addClassName("calendar-description");

        calendarSection.addClassName("Calendar-View");
        calendarSection.setPadding(false);
        calendarSection.setSpacing(false);

        calendarRow.addClassName("calendar-row");
        calendarRow.setWidthFull();
        calendarRow.setPadding(false);
        calendarRow.setSpacing(false);
        calendarRow.add(createCalendarPanel("calendar-panel--initial"));

        searchMessages.addClassName("calendar-chat");
        searchMessages.setPadding(false);
        searchMessages.setSpacing(false);

        searchInput.setPlaceholder("Buscar en tu semana");
        searchInput.addClassName("calendar-search-input");
        searchButton.addClassNames("action-btn", "action-btn--primary", "calendar-search-button");
        voiceButton.addClassNames("action-btn", "action-btn--voice", "calendar-voice-button");
        searchButton.addClickListener(event -> addSearchMessage());

        searchBar.addClassName("calendar-search");
        searchBar.setWidthFull();
        searchBar.add(searchInput, searchButton, voiceButton);

        calendarSection.add(calendarRow, searchMessages, searchBar);
        add(title, description, calendarSection);
    }

    private void addSearchMessage() {
        String phrase = searchInput.getValue() == null ? "" : searchInput.getValue().trim();
        if (phrase.isBlank()) {
            return;
        }

        Div message = new Div(new Span(phrase));
        message.addClassName("calendar-chat-message");
        searchMessages.add(message);
        searchInput.setValue("");
        expandCalendars();
    }

    private void expandCalendars() {
        if (calendarsExpanded) {
            return;
        }

        calendarsExpanded = true;
        calendarRow.removeAll();
        calendarRow.removeClassName("calendar-row");
        calendarRow.addClassName("calendar-triple");
        calendarRow.addClassName("calendar-triple--revealed");
        calendarRow.add(
                createCalendarPanel(),
                createCalendarPanel(),
                createCalendarPanel()
        );
    }

    private Component createCalendarPanel(String... additionalClasses) {
        VerticalLayout panel = new VerticalLayout(createCalendarHeader(), createCalendarGrid());
        panel.addClassName("calendar-panel");
        panel.addClassNames(additionalClasses);
        panel.setPadding(false);
        panel.setSpacing(false);
        return panel;
    }

    private Component createCalendarHeader() {
        H2 weekTitle = new H2("Semana del 19 al 25 de octubre");
        weekTitle.addClassName("calendar-week-title");

        Span weekStatus = new Span("Vista semanal");
        weekStatus.addClassName("calendar-week-status");

        VerticalLayout heading = new VerticalLayout(weekTitle, weekStatus);
        heading.addClassName("calendar-heading");
        heading.setPadding(false);
        heading.setSpacing(false);

        Div header = new Div(heading);
        header.addClassName("calendar-header");
        return header;
    }

    private Component createCalendarGrid() {
        Div calendar = new Div();
        calendar.addClassName("Calendar");
        calendar.add(createTimeColumn(), createDays());
        return calendar;
    }

    private Div createTimeColumn() {
        Div timeColumn = new Div();
        timeColumn.addClassName("calendar-time-column");

        Div headerSpace = new Div();
        headerSpace.addClassName("calendar-time-header");
        timeColumn.add(headerSpace);

        String[] times = {"08:00", "10:00", "12:00", "14:00", "16:00", "18:00"};
        for (String time : times) {
            Div label = new Div(new Span(time));
            label.addClassName("calendar-time-label");
            timeColumn.add(label);
        }
        return timeColumn;
    }

    private Div createDays() {
        Div days = new Div();
        days.addClassName("calendar-days");
        days.add(
                createDayColumn("LUN", "19", "Planificar semana", null, "Bloque de enfoque"),
                createDayColumn("MAR", "20", null, "Lectura", null),
                createDayColumn("MIÉ", "21", "Bloque creativo", null, null),
                createDayColumn("JUE", "22", null, "Revisión de avances", null),
                createDayColumn("VIE", "23", "Cierre semanal", null, "Reflexión"),
                createDayColumn("SÁB", "24", null, null, null),
                createDayColumn("DOM", "25", null, null, "Preparar nueva semana")
        );
        return days;
    }

    private Div createDayColumn(String day, String date, String... events) {
        Div dayColumn = new Div();
        dayColumn.addClassName("calendar-day-column");

        Div dayHeader = new Div();
        dayHeader.addClassName("calendar-day-header");
        dayHeader.add(new Span(day), new Span(date));
        dayColumn.add(dayHeader);

        Div dayBody = new Div();
        dayBody.addClassName("calendar-day-body");
        for (int slot = 0; slot < 6; slot++) {
            Div timeSlot = new Div();
            timeSlot.addClassName("calendar-time-slot");
            if (slot < events.length && events[slot] != null) {
                Span eventText = new Span(events[slot]);
                eventText.addClassName("calendar-event-text");

                Div event = new Div(eventText);
                event.addClassName("calendar-event");
                timeSlot.add(event);
            }
            dayBody.add(timeSlot);
        }
        dayColumn.add(dayBody);
        return dayColumn;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!sesion.estaAutenticado()) {
            event.forwardTo(LoginView.class);
        } else if (sesion.necesitaOnboarding()) {
            event.forwardTo(Onboarding.class);
        }
    }
}
