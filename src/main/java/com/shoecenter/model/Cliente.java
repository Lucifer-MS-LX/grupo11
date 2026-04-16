package com.shoecenter.model;

public class Cliente {
    private String nombre;
    private String direccion;
    private String telefono;
    private String documento;

    public Cliente(String nombre, String direccion, String telefono, String documento) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.documento = documento;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getDocumento() { return documento; }
}
