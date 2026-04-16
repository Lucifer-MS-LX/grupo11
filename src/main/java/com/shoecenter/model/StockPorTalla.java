package com.shoecenter.model;

public class StockPorTalla {
    private int talla;
    private int cantidad;

    public StockPorTalla(int talla, int cantidad) {
        this.talla = talla;
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public int getTalla() { return talla; }
    public void setTalla(int talla) { this.talla = talla; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}