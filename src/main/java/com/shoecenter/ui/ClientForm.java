package com.shoecenter.ui;

import com.shoecenter.model.Cliente;
import com.shoecenter.repository.ClientesDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ClientForm extends JDialog {
    private JTextField txtNombre, txtDir, txtTel, txtDoc;
    private JButton btnGuardar;

    private ClientesDAO clienteDAO;
    private Runnable onSuccess;
    private Cliente clienteActual;

    public ClientForm(Window owner, ClientesDAO dao, Cliente cliente, Runnable onSuccess) {
        super(owner, "Detalles Cliente", ModalityType.APPLICATION_MODAL);
        this.clienteDAO = dao;
        this.clienteActual = cliente;
        this.onSuccess = onSuccess;
        initUI();
    }

    private void initUI() {
        configureWindow();
        initializeComponents();
        setupEvents();
        configureLayout();
    }

    private void configureWindow() {
        setTitle(clienteActual != null ? "Actualizar Cliente" : "Nuevo Cliente");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void initializeComponents() {
        txtNombre = new JTextField(clienteActual != null ? clienteActual.getNombre() : "", 20);
        txtDir = new JTextField(clienteActual != null ? clienteActual.getDireccion() : "");
        txtTel = new JTextField(clienteActual != null ? clienteActual.getTelefono() : "");
        txtDoc = new JTextField(clienteActual != null ? clienteActual.getDocumento() : "");

        if (clienteActual != null) txtDoc.setEditable(false);

        btnGuardar = new JButton("Guardar");
    }

    private void setupEvents() {
        btnGuardar.addActionListener(e -> guardar());
    }

    private void configureLayout() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addComp(p, "Nombre:", txtNombre, gbc, 0);
        addComp(p, "Dirección:", txtDir, gbc, 1);
        addComp(p, "Teléfono:", txtTel, gbc, 2);
        addComp(p, "DNI/RUC:", txtDoc, gbc, 3);

        gbc.gridy = 4; gbc.gridx = 1;
        p.add(btnGuardar, gbc);

        setContentPane(p);
        pack();
        setLocationRelativeTo(getOwner());
    }

    private void addComp(JPanel p, String lbl, JTextField tf, GridBagConstraints gbc, int y) {
        gbc.gridy = y; gbc.gridx = 0; p.add(new JLabel(lbl), gbc);
        gbc.gridx = 1; p.add(tf, gbc);
    }

    private void guardar() {
        if (txtNombre.getText().trim().isEmpty() || txtDoc.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y Documento son obligatorios.");
            return;
        }
        clienteDAO.guardarCliente(new Cliente(
                txtNombre.getText().trim(), txtDir.getText().trim(),
                txtTel.getText().trim(), txtDoc.getText().trim()
        ));

        if (onSuccess != null) onSuccess.run();
        dispose();
    }
}