package com.shoecenter.ui;

import com.shoecenter.config.config;
import com.shoecenter.model.Producto;
import com.shoecenter.repository.ClientesDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class DetalleProductoForm extends JDialog {

    private final Producto producto;
    private final boolean esAdmin;
    private DefaultTableModel modeloCarrito;

    private JLabel lblImagen, lblStock, lblPrecio, lblCantidad, lblProducto, lblTalla;
    private JTextField txtStock, txtPrecio, txtCantidad;
    private JButton btnCarrito, btnTalla, btnImage;
    private Map<Integer, JRadioButton> radioButtonsTallas = new HashMap<>();
    private ButtonGroup groupTalla;

    public DetalleProductoForm(Frame owner, Producto producto, boolean esAdmin, DefaultTableModel modelo) {
        super(owner, "Detalle del producto", true);
        this.producto = producto;
        this.esAdmin = esAdmin;
        this.modeloCarrito = modelo;

        initializeComponents();
        setUpEvents();
        configureLayout();
        configureWindow();
    }

    private void configureWindow() {
        setTitle(producto.getMarca());
        if (esAdmin) {
            setSize(800, 350);
        } else {
            setSize(800, 280);
        }
        setLocationRelativeTo(getOwner());
    }

    private void initializeComponents() {
        // Textos y Fuentes
        lblProducto = new JLabel(producto.getMarca().toUpperCase());
        lblProducto.setFont(new Font("Tahoma", Font.BOLD, 30));

        lblTalla = new JLabel("Talla :");
        lblTalla.setFont(new Font("Tahoma", Font.BOLD, 18));

        lblStock = new JLabel("Stock:");
        lblStock.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblPrecio = new JLabel("Precio:");
        lblPrecio.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setFont(new Font("Tahoma", Font.BOLD, 15));

        // Campos (Iniciamos con la talla por defecto o la primera disponible)
        txtStock = new JTextField(String.valueOf(producto.getStockDeTalla(40)), 10);
        txtPrecio = new JTextField(String.valueOf(producto.getPrecio()), 10);
        txtCantidad = new JTextField("1", 5);

        if (!esAdmin) {
            txtStock.setEditable(false);
            txtPrecio.setEditable(false);
        }

        btnImage = new JButton("Modificar Imagen");
        btnCarrito = new JButton("Adicionar al Carrito");
        btnTalla = new JButton("Gestionar Tallas");

        // RadioButtons

        groupTalla = new ButtonGroup();
        radioButtonsTallas.clear();

        // Supongamos que producto.getTallasDisponibles() devuelve una lista [40, 42, 44]
        for (Integer talla : producto.getTallasDisponibles()) {
            JRadioButton rdbtn = new JRadioButton(String.valueOf(talla));

            // Seleccionar el primero por defecto
            if (radioButtonsTallas.isEmpty()) rdbtn.setSelected(true);

            groupTalla.add(rdbtn);
            radioButtonsTallas.put(talla, rdbtn);
        }

        try {
            ImageIcon icon = new ImageIcon(config.PATH + producto.getFoto());
            Image img = icon.getImage().getScaledInstance(275, 150, Image.SCALE_SMOOTH);
            lblImagen = new JLabel(new ImageIcon(img));
        } catch (Exception e) {
            lblImagen = new JLabel("Imagen no encontrada");
        }
    }

    private void setUpEvents() {
        // Evento para actualizar stock según talla seleccionada
        ActionListener tallaListener = e -> {
            int talla = Integer.parseInt(((JRadioButton)e.getSource()).getText());
            txtStock.setText(String.valueOf(producto.getStockDeTalla(talla)));
        };

        // Aplicar a todos los botones generados
        radioButtonsTallas.values().forEach(btn -> btn.addActionListener(tallaListener));

        btnCarrito.addActionListener(e -> {
            try {
                int cant = Integer.parseInt(txtCantidad.getText());
                double pu = producto.getPrecio();
                double importe = cant * pu;

                modeloCarrito.addRow(new Object[]{
                        cant,
                        producto.getMarca(),
                        pu,
                        importe
                });

                new PuntodeVentaForm(new ClientesDAO(), modeloCarrito).setVisible(true);
                this.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese una cantidad válida.");
            }
        });
    }

    private void configureLayout() {
        setLayout(new BorderLayout());
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Izquierdo (Imagen y Datos Técnicos)
        gbc.gridx = 0; gbc.weightx = 0.6;
        panelPrincipal.add(crearPanelIzquierdo(), gbc);

        // Derecho (Marca, Tallas, Compra)
        gbc.gridx = 1; gbc.weightx = 0.4;
        panelPrincipal.add(crearPanelDerecho(), gbc);
        add(panelPrincipal, BorderLayout.CENTER);
        pack();
    }

    private JPanel crearPanelIzquierdo() {

        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;

        JPanel panelSuperior = new JPanel(new GridBagLayout());
        GridBagConstraints gbs = new GridBagConstraints();

        gbs.insets = new Insets(5, 5, 5, 15);
        gbs.fill = GridBagConstraints.HORIZONTAL;
        gbs.anchor = GridBagConstraints.NORTH;

        gbs.gridx = 0;
        gbs.gridy = 0;
        panelSuperior.add(lblImagen, gbs);

        if (esAdmin) {
            gbs.gridy = 1;
            panelSuperior.add(btnImage,gbs);
        }

        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        panelPrincipal.add(panelSuperior,gbc);
        gbc.gridwidth = 1;

        JPanel panelLeft = new JPanel(new GridBagLayout());
        GridBagConstraints gbl = new GridBagConstraints();

        gbl.insets = new Insets(5, 5, 5, 15);
        gbl.fill = GridBagConstraints.HORIZONTAL;
        gbl.anchor = GridBagConstraints.NORTH;

        gbl.gridx = 0;
        gbl.gridy = 0;
        panelLeft.add(lblStock, gbl);
        gbl.gridy = 1;
        gbl.weightx = 1;
        panelLeft.add(txtStock, gbl);

        JPanel panelRight = new JPanel(new GridBagLayout());
        GridBagConstraints gbr = new GridBagConstraints();

        gbr.insets = new Insets(5, 5, 5, 15);
        gbr.fill = GridBagConstraints.HORIZONTAL;
        gbr.anchor = GridBagConstraints.NORTH;

        gbr.gridx = 0;
        gbr.gridy = 0;
        panelRight.add(lblPrecio, gbr);
        gbr.gridy = 1;
        gbr.weightx = 1;
        panelRight.add(txtPrecio, gbr);

        if (esAdmin) {
            gbl.gridy = 2;
            panelLeft.add(new JButton("Modificar Stock"), gbl);
            gbr.gridy = 2;
            panelRight.add(new JButton("Modificar Precio"), gbr);
        }

        gbc.gridy = 1;
        gbc.gridx = 0;
        panelPrincipal.add(panelLeft, gbc);
        gbc.gridy = 1;
        gbc.gridx = 1;
        panelPrincipal.add(panelRight,gbc);

        return panelPrincipal;
    }

    private JPanel crearPanelDerecho() {
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelPrincipal.add(lblProducto,gbc);

        // ----------------------TALLA --------------------------
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelPrincipal.add(lblTalla,gbc);

        JPanel panelTalla = new JPanel(new FlowLayout(FlowLayout.LEFT)); // FlowLayout es más fácil para filas dinámicas

        for (JRadioButton rb : radioButtonsTallas.values()) {
            panelTalla.add(rb);
        }

        gbc.gridx = 1;
        gbc.gridy = 1;
        panelPrincipal.add(panelTalla,gbc);

        if (esAdmin) {
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            gbc.gridy = 2;
            gbc.weightx = 1;
            panelPrincipal.add(btnTalla,gbc);
        }

        // ----------------------TALLA --------------------------


        gbc.gridy = (esAdmin) ? 3:2 ;
        gbc.gridx = 0;
        panelPrincipal.add(lblCantidad,gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panelPrincipal.add(txtCantidad,gbc);
        gbc.gridy = (esAdmin) ? 4:3 ;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panelPrincipal.add(btnCarrito,gbc);
        return panelPrincipal;
    }
}