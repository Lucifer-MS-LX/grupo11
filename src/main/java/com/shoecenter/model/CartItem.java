package com.shoecenter.model;

import java.math.BigDecimal;

public class CartItem {

    private final Producto producto;
    private final int talla;
    private int cantidad;

    public CartItem(Producto producto, int talla, int cantidad) {
        this.producto = producto;
        this.talla = talla;
        this.cantidad = cantidad;
    }

    public void incrementarCantidad(int cantidadExtra) {
        cantidad += cantidadExtra;
    }

    public BigDecimal getSubtotal() {
        return producto.getPrecio().multiply(BigDecimal.valueOf(cantidad));
    }

    public Producto getProducto() {
        return producto;
    }

    public int getTalla() {
        return talla;
    }

    public int getCantidad() {
        return cantidad;
    }
}
