package com.shoecenter.service;

import com.shoecenter.model.Producto;
import com.shoecenter.repository.ProductoDAO;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ProductoService {
    private final ProductoDAO productoDAO = new ProductoDAO();
    public boolean esSeleccionValida(Integer talla, String cantidadStr, Producto producto) {
        if (talla == null) {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione una talla.");
            return false;
        }
        try {
            int cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(null, "Cantidad invalida.");
                return false;
            }

            if (cantidad > producto.getStockDeTalla(talla)) {
                JOptionPane.showMessageDialog(null, "No hay suficiente stock disponible.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Cantidad inválida.");
            return false;
        }
    }

    public String obtenerStockPorTalla(Producto producto, Integer talla) {
        if (talla == null) return "0";
        return String.valueOf(producto.getStockDeTalla(talla));
    }

    public double calcularTotalCarrito(DefaultTableModel modelo) {
        double total = 0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            try {
                // El índice 3 es la columna "Subtotal" en tu modelo
                Object valor = modelo.getValueAt(i, 3);
                if (valor != null) {
                    total += Double.parseDouble(valor.toString());
                }
            } catch (Exception e) {
                System.err.println("Error sumando fila " + i);
            }
        }
        return total;
    }

    public void gestionarAdicionCarrito(DefaultTableModel modelo, String descripcion, int cantNueva, double precio, int stockMax) {
        int filaExistente = -1;

        // 1. Buscamos si el producto (Marca + Talla) ya está en la tabla
        for (int i = 0; i < modelo.getRowCount(); i++) {
            if (modelo.getValueAt(i, 1).toString().equals(descripcion)) {
                filaExistente = i;
                break;
            }
        }

        if (filaExistente != -1) {
            // 2. Si existe, calculamos la acumulación
            int cantActual = (int) modelo.getValueAt(filaExistente, 0);
            int totalAcumulado = cantActual + cantNueva;

            // Validamos el stock contra el total que habría en el carrito
            if (totalAcumulado > stockMax) {
                throw new IllegalArgumentException("No se puede agregar: El total en carrito ("
                        + totalAcumulado + ") excedería el stock (" + stockMax + ").");
            }

            // 3. Actualizamos la fila (Cantidad e Importe)
            modelo.setValueAt(totalAcumulado, filaExistente, 0);
            modelo.setValueAt(totalAcumulado * precio, filaExistente, 3);
        } else {
            // 4. Si es nuevo, validación simple y agregar
            if (cantNueva > stockMax) throw new IllegalArgumentException("Stock insuficiente.");
            modelo.addRow(new Object[]{cantNueva, descripcion, precio, cantNueva * precio});
        }
    }

    public boolean actualizarPrecio(int idProducto, String precioStr) {
        try {
            double nuevoPrecio = Double.parseDouble(precioStr);
            if (nuevoPrecio <= 0) return false;
            return productoDAO.actualizarPrecio(idProducto, nuevoPrecio);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean actualizarStock(int idProducto, int talla, String stockStr) {
        try {
            int nuevoStock = Integer.parseInt(stockStr);
            if (nuevoStock < 0) return false;
            // Aquí iría tu llamada al DAO: productoDAO.updateStock(idProducto, talla, nuevoStock);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}