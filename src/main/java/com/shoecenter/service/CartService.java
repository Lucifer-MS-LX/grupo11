package com.shoecenter.service;

import com.shoecenter.model.CartItem;
import com.shoecenter.model.Producto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CartService {

    private final List<CartItem> items;

    public CartService() {
        items = new ArrayList<>();
    }

    public void agregarProducto(Producto producto, int talla, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        int stockDisponible = producto.getStockDeTalla(talla) - getCantidadReservada(producto.getId(), talla);
        if (stockDisponible < cantidad) {
            throw new IllegalArgumentException("No hay stock suficiente para la talla seleccionada.");
        }

        for (CartItem item : items) {
            if (item.getProducto().getId().equals(producto.getId()) && item.getTalla() == talla) {
                item.incrementarCantidad(cantidad);
                return;
            }
        }

        items.add(new CartItem(producto, talla, cantidad));
    }

    public void eliminarItem(int index) {
        items.remove(index);
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getCantidadItems() {
        return items.stream().mapToInt(CartItem::getCantidad).sum();
    }

    public BigDecimal getTotal() {
        return items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void confirmarVenta() {
        for (CartItem item : items) {
            if (item.getProducto().getStockDeTalla(item.getTalla()) < item.getCantidad()) {
                throw new IllegalStateException("El stock cambio antes de procesar la venta.");
            }
        }
        for (CartItem item : items) {
            item.getProducto().descontarStock(item.getTalla(), item.getCantidad());
        }
        clear();
    }

    public void clear() {
        items.clear();
    }

    private int getCantidadReservada(String productoId, int talla) {
        return items.stream()
                .filter(item -> item.getProducto().getId().equals(productoId) && item.getTalla() == talla)
                .mapToInt(CartItem::getCantidad)
                .sum();
    }
}
