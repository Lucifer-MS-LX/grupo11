package com.shoecenter.ui;

import com.shoecenter.model.Producto;
import com.shoecenter.repository.ProductoDAO;

import javax.swing.*;
import java.awt.*;

public class AddProducto extends JDialog {

    private JLabel lblNombre, lblFoto, lblPrecio, lblGenero;
    private JTextField txtNombre, txtfoto, txtprecio;
    private JRadioButton rbHombre, rbMujer;
    private JButton btnGuardar, btnCancelar;

    private final ProductoDAO productoDAO = new ProductoDAO();

    public AddProducto(Frame parent) {
        super(parent, "Registro de Nuevo Producto", true);
        initAddProductoUI(parent);
    }

    private void initAddProductoUI(Frame parent) {
        configureWindow(parent);
        initializeComponents();
        configureLayout();
        setupEvents();
    }

    private void configureWindow(Frame parent) {
        setSize(350, 240);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
    }

    private void initializeComponents() {

        lblNombre = new JLabel("Nombre del Producto :");
        lblFoto = new JLabel("Nombre de la Imagen :");
        lblPrecio = new JLabel("Precio :");
        lblGenero = new JLabel("Genero");

        txtNombre = new JTextField(10);
        txtfoto = new JTextField(10);
        txtprecio = new JTextField(10);

        rbHombre = new JRadioButton("Hombre", true);
        rbMujer = new JRadioButton("Mujer");
        ButtonGroup grupoGenero = new ButtonGroup();
        grupoGenero.add(rbHombre);
        grupoGenero.add(rbMujer);

        btnGuardar = new JButton("Guardar Producto");
        btnCancelar = new JButton("Cancelar");
    }

    private void configureLayout() {
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelPrincipal.add(lblNombre, gbc);
        gbc.gridx = 1;
        panelPrincipal.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelPrincipal.add(lblFoto, gbc);
        gbc.gridx = 1;
        panelPrincipal.add(txtfoto, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelPrincipal.add(lblPrecio, gbc);
        gbc.gridx = 1;
        panelPrincipal.add(txtprecio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panelPrincipal.add(lblGenero,gbc);
        JPanel panelGenero = new JPanel(new GridLayout(1,2));
        panelGenero.add(rbHombre);
        panelGenero.add(rbMujer);
        gbc.gridx = 1;
        panelPrincipal.add(panelGenero, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panelPrincipal.add(btnGuardar, gbc);
        gbc.gridx = 1;
        panelPrincipal.add(btnCancelar,gbc);
        add(panelPrincipal);
    }

    private void setupEvents() {
        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> {
            validarYGuardar();
        });
    }

    private void validarYGuardar() {
        try {
            String nombre = txtNombre.getText().trim();
            String foto = txtfoto.getText().trim();
            double precio = Double.parseDouble(txtprecio.getText().trim());
            String genero = rbHombre.isSelected() ? "Hombre" : "Mujer";

            if (nombre.isEmpty() || foto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
                return;
            }

            Producto nuevo = new Producto(0,nombre, genero, precio, foto);

            if (productoDAO.insertar(nuevo)) {
                JOptionPane.showMessageDialog(this, "Producto registrado correctamente.");
                dispose(); // Cierra el JDialog
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar en la base de datos.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número válido.");
        }
    }
}