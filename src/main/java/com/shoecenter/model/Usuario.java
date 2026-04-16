package com.shoecenter.model;

public class Usuario {

    private String username;
    private String password; // Campo nuevo
    private String rol;

    public Usuario(String username, String password, String rol) {
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    // Getters necesarios para la validación
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRol() { return rol; }
}

