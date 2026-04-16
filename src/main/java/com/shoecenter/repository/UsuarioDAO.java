package com.shoecenter.repository;

import com.shoecenter.model.Usuario;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private List<Usuario> listaUsuarios;

    public UsuarioDAO() {
        listaUsuarios = new ArrayList<>();
        // Simulamos dos usuarios para probar los roless
        listaUsuarios.add(new Usuario("admin", "1234", "ADMIN"));
        listaUsuarios.add(new Usuario("vendedora", "abcd", "VENDEDOR"));
    }

    // Método para validar el login
    public Usuario validarAcceso(String user, String pass) {
        for (Usuario u : listaUsuarios) {
            // Comparamos usuario y contraseña
            if (u.getUsername().equals(user) && u.getPassword().equals(pass)) {
                return u; // Retorna el objeto usuario con su rol si coincide
            }
        }
        return null; // Si no coinciden, retorna nulo
    }
}