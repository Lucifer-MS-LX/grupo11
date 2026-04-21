package com.shoecenter.repository;

import com.shoecenter.config.DatabaseConnection;
import com.shoecenter.model.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public List<Producto> buscarPorGenero(String genero) {

        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.id, p.marca, p.genero, p.precio, p.foto, i.talla, i.cantidad " +
                "FROM productos p " +
                "LEFT JOIN inventario i ON p.id = i.producto_id " +
                "WHERE p.genero = ? " +
                "ORDER BY p.id";

        try (Connection con = DatabaseConnection.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, genero);

            try (ResultSet rs = ps.executeQuery()) {
                Producto productoActual = null;
                int idAnterior = -1;

                while (rs.next()) {
                    int idFila = rs.getInt("id");

                    if (idFila != idAnterior) {
                        productoActual = new Producto(
                                idFila,
                                rs.getString("marca"),
                                rs.getString("genero"),
                                rs.getDouble("precio"),
                                rs.getString("foto")
                        );
                        lista.add(productoActual);
                        idAnterior = idFila;
                    }

                    int talla = rs.getInt("talla");
                    if (talla > 0) {
                        productoActual.agregarStock(talla, rs.getInt("cantidad"));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error no genero encontrado: " + e.getMessage());
        }
        return lista;
    }

    public void actualizarStock(int productoId, int talla, int nuevoStock) {
        String sql = "UPDATE inventario SET cantidad = ? WHERE producto_id = ? AND talla = ?";

        try (Connection con = DatabaseConnection.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, nuevoStock);
            ps.setInt(2, productoId);
            ps.setInt(3, talla);

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Stock actualizado correctamente en la base de datos.");
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar el stock: " + e.getMessage());
        }
    }

    public boolean actualizarPrecio(int productoID, double nuevoPrecio){
        String sql = "UPDATE productos SET precio = ? WHERE id = ?";

        try (Connection con = DatabaseConnection.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, nuevoPrecio);
            ps.setInt(2, productoID);

            // Retornamos true si al menos una fila fue afectada
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el precio: " + e.getMessage());
            return false;
        }
    }

    public boolean insertar(Producto p) {
        String sql = "INSERT INTO productos (marca, genero, precio, foto) VALUES (?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getMarca());
            ps.setString(2, p.getGenero());
            ps.setDouble(3, p.getPrecio());
            ps.setString(4, p.getFoto());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
            return false;
        }
    }
}