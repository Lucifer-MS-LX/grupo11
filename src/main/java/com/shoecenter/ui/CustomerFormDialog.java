package com.shoecenter.ui;

import com.shoecenter.app.ApplicationContext;
import com.shoecenter.model.Cliente;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;

public class CustomerFormDialog extends JDialog {

    private final ApplicationContext context;
    private final Cliente cliente;
    private final Runnable onSave;

    private JTextField txtNombre;
    private JTextField txtDireccion;
    private JTextField txtTelefono;
    private JTextField txtDocumento;

    public CustomerFormDialog(Window owner, ApplicationContext context, Cliente cliente, Runnable onSave) {
        super(owner, "Detalles del cliente", ModalityType.APPLICATION_MODAL);
        this.context = context;
        this.cliente = cliente;
        this.onSave = onSave;
        initUI();
    }

    private void initUI() {
        txtNombre = new JTextField(cliente != null ? cliente.getNombre() : "");
        txtDireccion = new JTextField(cliente != null ? cliente.getDireccion() : "");
        txtTelefono = new JTextField(cliente != null ? cliente.getTelefono() : "");
        txtDocumento = new JTextField(cliente != null ? cliente.getDocumento() : "");

        if (cliente != null) {
            txtNombre.setEditable(false);
        }

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardarCliente());

        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelPrincipal.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panelPrincipal.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panelPrincipal.add(new JLabel("Direccion:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panelPrincipal.add(txtDireccion, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        panelPrincipal.add(new JLabel("Telefono:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panelPrincipal.add(txtTelefono, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        panelPrincipal.add(new JLabel("DNI/RUC:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panelPrincipal.add(txtDocumento, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 0;
        panelPrincipal.add(btnGuardar, gbc);

        setContentPane(panelPrincipal);
        pack();
        setLocationRelativeTo(getOwner());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void guardarCliente() {
        try {
            context.getCustomerService().guardarCliente(
                    txtNombre.getText().trim(),
                    txtDireccion.getText().trim(),
                    txtTelefono.getText().trim(),
                    txtDocumento.getText().trim()
            );
            onSave.run();
            dispose();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validacion", JOptionPane.WARNING_MESSAGE);
        }
    }
}
