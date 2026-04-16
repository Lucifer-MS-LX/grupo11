package com.shoecenter.ui;

import javax.swing.*;
import java.awt.*;

public class AddProducto extends JFrame {

    // Definición de componentes
    private JTextField txtNombre, txtPath, txtMarca, txtTalla;
    private JComboBox<String> comboColor;
    private JRadioButton rbHombre, rbMujer;

    public AddProducto() {
        // Configuración de la Ventana (JFrame)
        setTitle("Registro de Producto");
        setSize(550, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Componente: JPanel con LayoutManager
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 1: Nombre del Producto (JTextField)
        gbc.gridx = 0; gbc.gridy = 0;
        panelPrincipal.add(new JLabel("Nombre del Producto:"), gbc);
        txtNombre = new JTextField("Zapatillas Urbanas Hombre Breaknet", 20);
        gbc.gridx = 1;
        panelPrincipal.add(txtNombre, gbc);

        // Fila 2: Imagen Path (JTextField)
        gbc.gridx = 0; gbc.gridy = 1;
        panelPrincipal.add(new JLabel("Imagen path:"), gbc);
        txtPath = new JTextField("/home/shoecenter/adidas_breaknet");
        gbc.gridx = 1;
        panelPrincipal.add(txtPath, gbc);

        // Fila 3: Marca (JTextField - Modelo de Datos)
        gbc.gridx = 0; gbc.gridy = 2;
        panelPrincipal.add(new JLabel("Marca:"), gbc);
        txtMarca = new JTextField("Adidas");
        gbc.gridx = 1;
        panelPrincipal.add(txtMarca, gbc);

        // Fila 4: Color (JComboBox) y Talla (Validación Input)
        JPanel filaExtras = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        filaExtras.add(new JLabel("Color: "));
        comboColor = new JComboBox<>(new String[]{"Blanco", "Negro", "Azul"});
        filaExtras.add(comboColor);

        filaExtras.add(new JLabel("   Talla: "));
        txtTalla = new JTextField(5);
        filaExtras.add(txtTalla);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panelPrincipal.add(filaExtras, gbc);

        // Fila 5: Género (JRadioButton - Grupo de Botones)
        JPanel panelGenero = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rbHombre = new JRadioButton("Hombre", true);
        rbMujer = new JRadioButton("Mujer");
        ButtonGroup grupoGenero = new ButtonGroup();
        grupoGenero.add(rbHombre);
        grupoGenero.add(rbMujer);
        panelGenero.add(rbHombre);
        panelGenero.add(rbMujer);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panelPrincipal.add(panelGenero, gbc);

        // Añadir panel a la ventana
        add(panelPrincipal);
    }
}
