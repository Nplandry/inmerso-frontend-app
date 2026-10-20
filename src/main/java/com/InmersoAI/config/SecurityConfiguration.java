package com.InmersoAI.config;

import com.InmersoAI.ui.views.auth.LoginView;
import com.InmersoAI.ui.views.auth.Onboarding;
import com.InmersoAI.ui.views.focus.FocusView;
import com.InmersoAI.ui.views.settings.SettingsView;
import com.vaadin.flow.server.auth.AnnotatedViewAccessChecker;
import com.vaadin.flow.spring.security.NavigationAccessControlConfigurer;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

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

        // Handlers con logs para rastrear el exito o fallo del POST al login
        http.formLogin(form -> form
                .loginProcessingUrl("/login")
                .successHandler(successHandler())
                .failureHandler(failureHandler()));

        return http.build();
    }

    private AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            log.info("[Auth] Login EXITOSO para usuario='{}'. Redirigiendo a la raiz.",
                    authentication.getName());
            response.sendRedirect(request.getContextPath() + "/");
        };
    }

    private AuthenticationFailureHandler failureHandler() {
        SimpleUrlAuthenticationFailureHandler handler =
                new SimpleUrlAuthenticationFailureHandler("/Login?error");
        return (request, response, exception) -> {
            log.warn("[Auth] Login FALLIDO: {}", exception.getMessage());
            handler.onAuthenticationFailure(request, response, exception);
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
