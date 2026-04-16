package com.shoecenter.service;

import com.shoecenter.model.Usuario;
import com.shoecenter.repository.UsuarioDAO;

import java.util.Arrays;
import java.util.Optional;

public class AuthService {

    private final UsuarioDAO usuarioDAO;

    public AuthService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public Optional<Usuario> autenticar(String username, char[] password) {
        try {
            return usuarioDAO.validarAcceso(username, password);
        } finally {
            Arrays.fill(password, '\0');
        }
    }
}
