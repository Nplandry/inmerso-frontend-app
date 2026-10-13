package com.InmersoAI.domain;

public class Usuario {
    public static final String ROLE_USER = "ROLE_USER";

    private Long id;
    private String username;
    private String password;
    private String role;

    public Usuario(Long id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}