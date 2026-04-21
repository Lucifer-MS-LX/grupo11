package com.shoecenter.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Producto {
    private int id;
    private String marca;
    private String genero;
    private double precio;
    private String foto;
    private List<StockPorTalla> inventario;

    public Producto(int id, String marca, String genero, double precio, String foto) {
        this.id = id;
        this.marca = marca;
        this.genero = genero;
        this.precio = precio;
        this.foto = foto;
        this.inventario = new ArrayList<>();
    }

    public void agregarStock(int talla, int cantidad) {
        this.inventario.add(new StockPorTalla(talla, cantidad));
    }

    public int getStockDeTalla(int talla) {
        for (StockPorTalla st : inventario) {
            if (st.getTalla() == talla) {
                return st.getCantidad();
            }
        }
        return 0;
    }

    public Set<Integer> getTallasDisponibles() {
        return inventario.stream()
                .map(StockPorTalla::getTalla)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public void setStockParaTalla(int talla, int nuevaCantidad) {
        for (StockPorTalla st : inventario) {
            if (st.getTalla() == talla) {
                st.setCantidad(nuevaCantidad);
                return;
            }
        }
        // Si la talla no existía, la agregamos
        this.agregarStock(talla, nuevaCantidad);
    }

    public String getMarca() { return marca; }
    public double getPrecio() { return precio; }
    public String getGenero() { return genero; }
    public String getFoto() { return foto; }
    public int getId() { return id; }
    public void setPrecio(double precio) { this.precio = precio; }
}