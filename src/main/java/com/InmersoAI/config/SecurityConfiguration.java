package com.InmersoAI.config;

import com.InmersoAI.ui.views.auth.LoginView;
import com.InmersoAI.ui.views.auth.Onboarding;
import com.InmersoAI.ui.views.focus.FocusView;
import com.InmersoAI.ui.views.settings.SettingsView;
import com.vaadin.flow.server.auth.AnnotatedViewAccessChecker;
import com.vaadin.flow.spring.security.NavigationAccessControlConfigurer;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    NavigationAccessControlConfigurer navigationAccessControlConfigurer() {
        AnnotatedViewAccessChecker annotatedViewAccessChecker = new AnnotatedViewAccessChecker();
        return new NavigationAccessControlConfigurer()
                .withNavigationAccessChecker(context -> {
                    Class<?> navigationTarget = context.getNavigationTarget();
                    if (navigationTarget == LoginView.class || navigationTarget == Onboarding.class || navigationTarget == FocusView.class
                        || navigationTarget == SettingsView.class) {
                        return context.allow();
                    }
                    return annotatedViewAccessChecker.check(context);
                })
                .withLoginView(LoginView.class);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.with(VaadinSecurityConfigurer.vaadin(), configurer -> configurer.loginView(LoginView.class));
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
