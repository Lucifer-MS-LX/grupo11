package com.shoecenter.repository;

import com.shoecenter.model.Cliente;
import java.util.HashMap;
import java.util.Collection;

public class ClientesDAO {
    // El DAO centraliza los datos
    private HashMap<String, Cliente> clientes;

    public ClientesDAO() {
        clientes = new HashMap<>();
        // Datos de prueba iniciales
        guardarCliente(new Cliente("Cliente 1", "Av. Circunvalación", "99999999", "12345678"));
        guardarCliente(new Cliente("Cliente 2", "Av. Arenales", "99999998", "23456789"));
        guardarCliente(new Cliente("Cliente 3", "Av. La Molina", "99999997", "34567890"));
    }

    public void guardarCliente(Cliente cliente) {
        clientes.put(cliente.getNombre(), cliente);
    }

    public Cliente obtenerCliente(String nombre) {
        return clientes.get(nombre);
    }

    public Collection<String> obtenerNombresClientes() {
        return clientes.keySet();
    }
}
