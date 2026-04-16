package com.shoecenter.service;

import com.shoecenter.model.Cliente;
import com.shoecenter.repository.ClientesDAO;

import java.util.List;
import java.util.Optional;

public class CustomerService {

    private final ClientesDAO clientesDAO;

    public CustomerService(ClientesDAO clientesDAO) {
        this.clientesDAO = clientesDAO;
    }

    public List<Cliente> listarClientes() {
        return clientesDAO.obtenerClientes();
    }

    public Optional<Cliente> buscarPorNombre(String nombre) {
        return clientesDAO.obtenerClientePorNombre(nombre);
    }

    public void guardarCliente(String nombre, String direccion, String telefono, String documento) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        Cliente existente = buscarPorNombre(nombre.trim()).orElse(null);
        if (existente == null) {
            clientesDAO.guardarCliente(new Cliente(nombre.trim(), limpiar(direccion), limpiar(telefono), limpiar(documento)));
            return;
        }
        clientesDAO.eliminarCliente(existente);
        existente.actualizarDatos(limpiar(direccion), limpiar(telefono), limpiar(documento));
        clientesDAO.guardarCliente(existente);
    }

    private String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
