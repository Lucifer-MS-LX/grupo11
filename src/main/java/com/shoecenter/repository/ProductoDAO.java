package com.shoecenter.repository;

import com.shoecenter.model.Producto;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductoDAO {
    private List<Producto> inventario;

    public ProductoDAO() {
        inventario = new ArrayList<>();
        cargarDatosSimulados();
    }

    private void cargarDatosSimulados() {
        // --- PRODUCTOS DE HOMBRE ---
        Producto h1 = new Producto("Adidas Originals Samba Og", "Hombre", 343.20, "h01.png");
        h1.agregarStock(40, 5); h1.agregarStock(42, 12); h1.agregarStock(44, 3);
        inventario.add(h1);

        Producto h2 = new Producto("Pacific Urbanas V", "Hombre", 169.90, "h02.png");
        h2.agregarStock(40, 8); h2.agregarStock(42, 5); h2.agregarStock(44, 10);
        inventario.add(h2);

        Producto h3 = new Producto("Adidas Urbanas Court", "Hombre", 127.20, "h03.png");
        h3.agregarStock(40, 12); h3.agregarStock(42, 11);
        inventario.add(h3);

        Producto h4 = new Producto("Adidas Urbanas Originals ", "Hombre", 251.30, "h04.png");
        h4.agregarStock(40, 20); h4.agregarStock(42,15);
        inventario.add(h4);

        Producto h5 = new Producto("Puma Urbanas Tifosi", "Hombre", 209.30, "h05.png");
        h5.agregarStock(40, 16); h5.agregarStock(42, 12);h5.agregarStock(44, 19);
        inventario.add(h5);

        Producto h6 = new Producto("North Star Urbanas Maisy", "Hombre", 129.90, "h06.png");
        h6.agregarStock(40, 13); h6.agregarStock(42, 7);h6.agregarStock(44, 11);
        inventario.add(h6);

        // --- PRODUCTOS DE MUJER ---
        Producto w1 = new Producto("Adidas Runblaze", "Mujer", 179.00, "w01.png");
        w1.agregarStock(38, 15); w1.agregarStock(36, 20);
        inventario.add(w1);

        Producto w2 = new Producto("Puma Skyrocket Lite 2A", "Mujer", 139.30, "w02.png");
        w2.agregarStock(38, 4); w2.agregarStock(36, 7);
        inventario.add(w2);

        Producto w3 = new Producto("Basement Urbanas", "Mujer", 119.90, "w03.png");
        w3.agregarStock(38, 8); w3.agregarStock(36, 11);
        inventario.add(w3);

        Producto w4 = new Producto("Adidas Urbanas Court 3", "Mujer", 259.00, "w04.png");
        w4.agregarStock(38, 6); w4.agregarStock(36, 7);
        inventario.add(w4);

        Producto w5 = new Producto("Adidas Urbanas Originals Samba", "Mujer", 343.20, "w05.png");
        w5.agregarStock(38, 10); w5.agregarStock(36, 5);
        inventario.add(w5);
    }

    // Método para filtrar por género (usando Streams para que sea más moderno)
    public List<Producto> buscarPorGenero(String genero) {
        return inventario.stream()
                .filter(p -> p.getGenero().equalsIgnoreCase(genero))
                .collect(Collectors.toList());
    }

    // Método para actualizar stock (Simulación de UPDATE)
    public void actualizarStock(String marca, int talla, int nuevoStock) {
        for (Producto p : inventario) {
            if (p.getMarca().equals(marca)) {
                p.getInventario().forEach(s -> {
                    if (s.getTalla() == talla) {
                        s.setCantidad(nuevoStock);
                    }
                });
            }
        }
    }
}