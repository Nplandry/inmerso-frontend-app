package com.InmersoAI.domain.model;

public class Usuario {
    public static final String ROLE_USER = "ROLE_USER";

    private Long id;
    private String username;
    private String password;
    private String role;
    private boolean onboardingCompletado;

    public Usuario(Long id, String username, String password, String role) {
        this(id, username, password, role, false);
    }

    public Usuario(Long id, String username, String password, String role, boolean onboardingCompletado) {
        this.id = id;
        this.username = username;
        this.password = password;
        setRole(role);
        this.onboardingCompletado = onboardingCompletado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if (!ROLE_USER.equals(role)) {
            throw new IllegalArgumentException("El único rol disponible es ROLE_USER");
        }
        this.role = role;
    }

    public boolean isOnboardingCompletado() {
        return onboardingCompletado;
    }

    public void completarOnboarding() {
        onboardingCompletado = true;
    }
}