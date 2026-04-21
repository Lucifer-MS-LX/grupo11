package com.shoecenter.repository;

import com.shoecenter.config.DatabaseConnection;
import com.shoecenter.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {
    public static Usuario validarAcceso(String user, String pass) {
        Usuario usuarioEncontrado = null;

        String sql = "SELECT username, password, rol FROM usuarios WHERE username = ? AND password = ?";

        try (Connection con = DatabaseConnection.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user);
            ps.setString(2, pass);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuarioEncontrado = new Usuario(
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("rol")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }

        return usuarioEncontrado;
    }
}