package com.shoecenter.service;

import com.shoecenter.model.Producto;
import com.shoecenter.repository.ProductoDAO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ProductService {

    private final ProductoDAO productoDAO;

    public ProductService(ProductoDAO productoDAO) {
        this.productoDAO = productoDAO;
    }

    public List<Producto> listarPorGenero(String genero) {
        return productoDAO.buscarPorGenero(genero);
    }

    public Producto registrarProducto(String nombre, String genero, BigDecimal precio, String foto, int talla, int stock) {
        validarDatos(nombre, genero, precio, foto, talla, stock);
        return productoDAO.crearProducto(nombre.trim(), genero.trim(), precio, foto.trim(), talla, stock);
    }

    public void actualizarProducto(Producto producto, BigDecimal precio, int talla, int stock) {
        if (precio.signum() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        }
        if (talla <= 0) {
            throw new IllegalArgumentException("La talla debe ser positiva.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        producto.setPrecio(precio);
        producto.setStock(talla, stock);
    }

    public void agregarTalla(Producto producto, int talla, int stock) {
        if (talla <= 0) {
            throw new IllegalArgumentException("La talla debe ser positiva.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        producto.setStock(talla, stock);
    }

    public Optional<Producto> buscarPorId(String id) {
        return productoDAO.buscarPorId(id);
    }

    private void validarDatos(String nombre, String genero, BigDecimal precio, String foto, int talla, int stock) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (genero == null || genero.trim().isEmpty()) {
            throw new IllegalArgumentException("El genero es obligatorio.");
        }
        if (foto == null || foto.trim().isEmpty()) {
            throw new IllegalArgumentException("La imagen es obligatoria.");
        }
        if (precio == null || precio.signum() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        }
        if (talla <= 0) {
            throw new IllegalArgumentException("La talla debe ser positiva.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
    }
}
