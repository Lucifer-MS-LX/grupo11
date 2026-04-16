package com.shoecenter.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Producto {
    private String marca;
    private String genero;
    private double precio;
    private String foto;
    private List<StockPorTalla> inventario;

    public Producto(String marca, String genero, double precio, String foto) {
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
        // Recorremos el inventario y extraemos solo el número de la talla
        return inventario.stream()
                .map(StockPorTalla::getTalla)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public String getMarca() { return marca; }
    public String getGenero() { return genero; }
    public double getPrecio() { return precio; }
    public String getFoto() { return foto; }
    public List<StockPorTalla> getInventario() { return inventario; }
}