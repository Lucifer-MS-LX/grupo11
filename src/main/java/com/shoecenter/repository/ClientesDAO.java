package com.shoecenter.repository;

import com.shoecenter.model.Cliente;
import com.shoecenter.config.DatabaseConnection; // Importa tu clase de conexión
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientesDAO {

    public void guardarCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, direccion, telefono, documento) VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), direccion = VALUES(direccion), telefono = VALUES(telefono)";

        try (Connection conn = DatabaseConnection.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (conn == null) return; // Validación de seguridad

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDireccion());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getDocumento());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Cliente obtenerClientePorDocumento(String documento) {
        String sql = "SELECT * FROM clientes WHERE documento = ?";
        try (Connection conn = DatabaseConnection.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (conn == null) return null;

            ps.setString(1, documento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getString("nombre"),
                            rs.getString("direccion"),
                            rs.getString("telefono"),
                            rs.getString("documento")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY nombre ASC";
        try (Connection conn = DatabaseConnection.getConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (conn == null) return lista;

            while (rs.next()) {
                lista.add(new Cliente(
                        rs.getString("nombre"), rs.getString("direccion"),
                        rs.getString("telefono"), rs.getString("documento")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}