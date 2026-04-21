package com.shoecenter.ui;

import com.shoecenter.config.config;
import com.shoecenter.model.Producto;
import com.shoecenter.model.Usuario;
import com.shoecenter.service.ProductoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class DetalleProductoForm extends JDialog {

    private final Producto producto;
    private final boolean esAdmin;
    private DefaultTableModel modeloCarrito;
    private final ProductoService productoService = new ProductoService();


    private JLabel lblImagen, lblStock, lblPrecio, lblCantidad, lblProducto, lblTalla;
    private JTextField txtStock, txtPrecio, txtCantidad;
    private JButton btnCarrito, btnTalla, btnImage,btnModificarStock,btnModificarPrecio;
    private Map<Integer, JRadioButton> radioButtonsTallas = new HashMap<>();
    private ButtonGroup groupTalla;
    private final Usuario usuarioSesion;


    public DetalleProductoForm(Frame owner, Producto producto, Usuario usuario, DefaultTableModel modelo) {
        super(owner, "Detalle del producto", true);
        this.producto = producto;
        this.usuarioSesion = usuario;
        this.esAdmin = usuario.getRol().equalsIgnoreCase("ADMIN");
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
        instanciarEtiquetas();
        instanciarCamposTexto();
        instanciarBotones();
        construirSelectorTallas();
        cargarImagenProducto();
    }

    private void instanciarEtiquetas(){
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
    }

    private void instanciarCamposTexto(){
        txtStock = new JTextField(String.valueOf(producto.getStockDeTalla(40)), 10);
        txtPrecio = new JTextField(String.valueOf(producto.getPrecio()), 10);
        txtCantidad = new JTextField("1", 5);

        if (!esAdmin) {
            txtStock.setEditable(false);
            txtPrecio.setEditable(false);
        }
    }

    private void instanciarBotones() {
        btnImage = new JButton("Modificar Imagen");
        btnCarrito = new JButton("Adicionar al Carrito");
        btnTalla = new JButton("Gestionar Tallas");
        btnModificarStock = new JButton("Modificar Stock");
        btnModificarPrecio = new JButton("Modificar Precio");
    }

    private void construirSelectorTallas() {
        groupTalla = new ButtonGroup();
        radioButtonsTallas.clear();

        boolean primeraTalla = true;
        for (Integer talla : producto.getTallasDisponibles()) {
            JRadioButton rdbtn = new JRadioButton(String.valueOf(talla));
            rdbtn.setBackground(Color.WHITE);
            rdbtn.addActionListener(e -> actualizarStockVista());

            groupTalla.add(rdbtn);
            radioButtonsTallas.put(talla, rdbtn);

            if (primeraTalla) {
                rdbtn.setSelected(true);
                primeraTalla = false;
            }
        }
        actualizarStockVista();
    }

    private void actualizarStockVista() {
        Integer talla = obtenerTallaSeleccionada();
        if (talla != null) {
            int stockDisponible = producto.getStockDeTalla(talla);
            txtStock.setText(String.valueOf(stockDisponible));
        }
    }

    private Integer obtenerTallaSeleccionada() {
        for (Map.Entry<Integer, JRadioButton> entry : radioButtonsTallas.entrySet()) {
            if (entry.getValue().isSelected()) return entry.getKey();
        }
        return null;
    }

    private void cargarImagenProducto() {
        try {
            ImageIcon icon = new ImageIcon(config.PATH + producto.getFoto());
            Image img = icon.getImage().getScaledInstance(275, 150, Image.SCALE_SMOOTH);
            lblImagen = new JLabel(new ImageIcon(img));
        } catch (Exception e) {
            lblImagen = new JLabel("Imagen no encontrada");
        }
    }

    private void configureLayout() {
        setLayout(new BorderLayout());
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        gbc.gridx = 0; gbc.weightx = 0.6;
        panelPrincipal.add(crearPanelIzquierdo(), gbc);

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
            panelLeft.add(btnModificarStock, gbl);
            gbr.gridy = 2;
            panelRight.add(btnModificarPrecio, gbr);
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

    private void setUpEvents() {
        btnCarrito.addActionListener(e -> procesarNavegacionVenta());

        if (esAdmin) {
            btnModificarPrecio.addActionListener(e -> ejecutarCambioPrecio());
            btnModificarStock.addActionListener(e -> ejecutarCambioStock());
            btnTalla.addActionListener(e -> ejecutarGestionTallas());
            btnImage.addActionListener(e -> ejecutarCambioImagen());
        }
    }

    private void ejecutarCambioPrecio() {
        String nuevoPrecio = JOptionPane.showInputDialog(this, "Nuevo precio para " + producto.getMarca(), txtPrecio.getText());
        if (productoService.actualizarPrecio(producto.getId(), nuevoPrecio)) {
            txtPrecio.setText(nuevoPrecio);
            producto.setPrecio(Double.parseDouble(nuevoPrecio));
            JOptionPane.showMessageDialog(this, "Precio actualizado correctamente.");
        } else if (nuevoPrecio != null) {
            JOptionPane.showMessageDialog(this, "Error: Ingrese un precio válido mayor a 0.");
        }
    }

    private void ejecutarCambioStock() {
        Integer talla = obtenerTallaSeleccionada();
        if (talla == null) return;

        String nuevoStock = JOptionPane.showInputDialog(this, "Nuevo stock para talla " + talla, txtStock.getText());
        if (productoService.actualizarStock(producto.getId(), talla, nuevoStock)) {
            txtStock.setText(nuevoStock);
            producto.setStockParaTalla(talla, Integer.parseInt(nuevoStock));
            JOptionPane.showMessageDialog(this, "Stock actualizado con éxito.");
        } else if (nuevoStock != null) {
            JOptionPane.showMessageDialog(this, "Error: Ingrese un valor numérico válido.");
        }
    }

    private void ejecutarGestionTallas() {
        String nuevaTallaStr = JOptionPane.showInputDialog(this, "Ingrese la nueva talla a adicionar:");
        if (nuevaTallaStr != null && !nuevaTallaStr.isEmpty()) {
            // Lógica para enviar al Service y actualizar el selector de tallas
            JOptionPane.showMessageDialog(this, "Función de tallas en desarrollo.");
        }
    }

    private void ejecutarCambioImagen() {
        // Aquí podrías usar otro JFileChooser para seleccionar la imagen en tu Arch Linux
        JOptionPane.showMessageDialog(this, "Seleccione la nueva imagen del producto.");
    }

    private void procesarNavegacionVenta() {
        Integer talla = obtenerTallaSeleccionada();
        String cantStr = txtCantidad.getText();

        if (productoService.esSeleccionValida(talla, cantStr, producto)) {
            try {
                int cant = Integer.parseInt(cantStr);
                String desc = producto.getMarca() + " (T:" + talla + ")";
                int stockDisponible = producto.getStockDeTalla(talla);

                // Invocamos la lógica centralizada en el Service
                productoService.gestionarAdicionCarrito(modeloCarrito, desc, cant, producto.getPrecio(), stockDisponible);

                // Navegación mediante StarterForm
                StarterForm padre = (StarterForm) getOwner();
                PuntodeVentaForm ventanaVenta = padre.getPuntodeVenta();

                ventanaVenta.actualizarTotal();
                ventanaVenta.setVisible(true);
                this.dispose();

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación de Inventario", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}