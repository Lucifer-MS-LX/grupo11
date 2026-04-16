package com.shoecenter.ui;

import com.shoecenter.model.Cliente;
import com.shoecenter.repository.ClientesDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PuntodeVentaForm extends JFrame {

    private JLabel lblVendedor,lblCliente, lblImporte, lblAddress, lblPhone, lblPK;
    private JTextField txtVendedor, txtPrecio, txtAddress, txtPhone, txtPK;
    private JButton btnProcesar, btnSeguir, btnAdd, btnUpdate;
    private JComboBox<String> cbCliente;
    private JTable tablaSalida;
    private DefaultTableModel modeloCarrito;

    private ClientesDAO clienteDAO;


    public PuntodeVentaForm(ClientesDAO dao, DefaultTableModel modelo) {
        this.clienteDAO = dao;
        this.modeloCarrito = modelo;
        initUI();
        if (tablaSalida != null) {
            tablaSalida.setModel(this.modeloCarrito);
            actualizarTotal(); // Método para sumar los precios
        }
    }

    private void initUI() {
        configureWindow();
        initializeComponents();
        setupEvents();
        configureLayout();
    }

    private void configureWindow() {
        setTitle("PUNTO DE VENTA");
        setSize(750,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void initializeComponents(){
        lblVendedor = new JLabel("Vendedor :");
        lblCliente = new JLabel("Cliente :");
        lblImporte = new JLabel("Importe a Pagar:");
        lblAddress = new JLabel("Direccion:");
        lblPhone = new JLabel("Telefono:");
        lblPK = new JLabel("DNI/RUC:");

        txtVendedor = new JTextField(12);
        txtVendedor.setEditable(false);
        txtPrecio = new JTextField(12);
        txtPrecio.setEditable(false);
        txtAddress = new JTextField(12);
        txtAddress.setEditable(false);
        txtPhone = new JTextField(12);
        txtPhone.setEditable(false);
        txtPK = new JTextField(12);
        txtPK.setEditable(false);

        tablaSalida = new JTable();
        cbCliente = new JComboBox<>();

        btnSeguir = new JButton("Continuar Comprando");
        btnProcesar = new JButton("Proceder a Pagar");
        btnAdd = new JButton("Adicionar Cliente");
        btnUpdate = new JButton("Actualizar Cliente");

    }

    private void setupEvents(){
        actualizarComboBox();
        btnAdd.addActionListener(e -> addaction());
        btnUpdate.addActionListener(e -> updateaction());
        cbCliente.addActionListener(e-> selectCliente());
        btnSeguir.addActionListener(e -> {
            this.dispose(); // Cierra el Punto de Venta y regresas automáticamente al catálogo
        });

        btnProcesar.addActionListener(e -> {
            if (modeloCarrito.getRowCount() > 0) {
                // Pasamos el modeloCarrito actual al PaymentForm
                String totalActual = lblImporte.getText();
                PaymentForm ventanaPago = new PaymentForm(modeloCarrito,totalActual);
                ventanaPago.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                        "El carrito está vacío", "Aviso",
                        JOptionPane.WARNING_MESSAGE);
            }

            //JOptionPane.showMessageDialog(this, "Venta realizada con éxito");
            //modeloCarrito.setRowCount(0); // Borra todas las filas del modelo compartido
            //this.dispose();
        });
    }

    private void addaction() {
        mostrarDialogoFormulario(null);
    }

    private void updateaction() {
        String seleccion = (String) cbCliente.getSelectedItem();

        if ("Seleccione Cliente...".equals(seleccion)) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente de la lista.");
        } else {
            Cliente clienteExistente = clienteDAO.obtenerCliente(seleccion);
            mostrarDialogoFormulario(clienteExistente);
        }
    }

    private void selectCliente() {
        String seleccion = (String) cbCliente.getSelectedItem();

        if ( seleccion == null || "Seleccione Cliente...".equals(seleccion)) {
            //JOptionPane.showMessageDialog(this, "Seleccione un cliente de la lista.");
            txtAddress.setText("");
            txtPhone.setText("");
            txtPK.setText("");
            return;
        }
        Cliente clienteExistente = clienteDAO.obtenerCliente(seleccion);
        if (clienteExistente != null){
            txtAddress.setText(clienteExistente.getDireccion());
            txtPhone.setText(clienteExistente.getTelefono());
            txtPK.setText(clienteExistente.getDocumento());
        }
    }

    private void actualizarComboBox() {
        cbCliente.removeAllItems();
        cbCliente.addItem("Seleccione Cliente...");

        for (String nombre : clienteDAO.obtenerNombresClientes()) {
            cbCliente.addItem(nombre);
        }
    }

    private void actualizarTotal() {
        double total = 0;
        for (int i = 0; i < modeloCarrito.getRowCount(); i++) {
            // La columna 3 es el "Importe" (Cantidad * P.U.)
            total += (double) modeloCarrito.getValueAt(i, 3);
        }
        txtPrecio.setText(String.valueOf(total));
    }

    private void mostrarDialogoFormulario(Cliente cliente) {
        JDialog dialogo = new JDialog(this, "Detalles Cliente", true);

        // Componentes
        JLabel lblNombre = new JLabel(" Nombre:");
        JTextField txtNombre = new JTextField(cliente != null ? cliente.getNombre() : "");
        if (cliente != null) txtNombre.setEditable(false);

        JLabel lblDir = new JLabel(" Dirección:");
        JTextField txtDir = new JTextField(cliente != null ? cliente.getDireccion() : "");

        JLabel lblTel = new JLabel(" Telefono:");
        JTextField txtTel = new JTextField(cliente != null ? cliente.getTelefono() : "");

        JLabel lblDoc = new JLabel("DNI/RUC:");
        JTextField txtDoc = new JTextField(cliente != null ? cliente.getDocumento() : "");

        JButton btnGuardar = new JButton("Guardar");

        // Layout del Diálogo
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(6, 0, 6, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0; gbc.gridx = 0; panelPrincipal.add(lblNombre, gbc);
        gbc.gridx = 1; gbc.weightx = 1; panelPrincipal.add(txtNombre, gbc);

        gbc.gridy = 1; gbc.gridx = 0; gbc.weightx = 0; panelPrincipal.add(lblDir, gbc);
        gbc.gridx = 1; gbc.weightx = 1; panelPrincipal.add(txtDir, gbc);

        gbc.gridy = 2; gbc.gridx = 0; gbc.weightx = 0; panelPrincipal.add(lblTel, gbc);
        gbc.gridx = 1; gbc.weightx = 1; panelPrincipal.add(txtTel, gbc);

        gbc.gridy = 3; gbc.gridx = 0; gbc.weightx = 0; panelPrincipal.add(lblDoc, gbc);
        gbc.gridx = 1; gbc.weightx = 1; panelPrincipal.add(txtDoc, gbc);

        gbc.gridy = 4; gbc.gridx = 1; gbc.weightx = 0; panelPrincipal.add(btnGuardar, gbc);

        // Funcionalidad del btnGuarda
        btnGuardar.addActionListener(e -> {
            if (txtNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "El nombre es obligatorio.");
                return;
            }
            // Guardar en el DAO compartido
            clienteDAO.guardarCliente(new Cliente(
                    txtNombre.getText().trim(),
                    txtDir.getText().trim(),
                    txtTel.getText().trim(),
                    txtDoc.getText().trim()
            ));

            actualizarComboBox(); // Refrescar la venta principal
            dialogo.dispose();
        });

        dialogo.setContentPane(panelPrincipal);
        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }

    private void configureLayout(){
        JPanel panelPrincipal = new JPanel( new BorderLayout(10,12));
        panelPrincipal.setBorder(new EmptyBorder(15,20,15,20));

        JPanel panelSuperior = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(6,0,6,10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panelSuperior.add(lblVendedor,gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelSuperior.add(txtVendedor,gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panelSuperior.add(lblCliente,gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelSuperior.add(cbCliente,gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panelSuperior.add(btnAdd,gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panelSuperior.add(btnUpdate,gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelSuperior.add(lblAddress,gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        panelSuperior.add(txtAddress,gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        panelSuperior.add(lblPhone,gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1;
        panelSuperior.add(txtPhone,gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.weightx = 0;
        panelSuperior.add(lblPK,gbc);

        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.weightx = 1;
        panelSuperior.add(txtPK,gbc);

        JPanel panelInferior = new JPanel(new GridBagLayout());
        GridBagConstraints gbc1 = new GridBagConstraints();

        gbc1.insets = new Insets(6,0,6,10);
        gbc1.anchor = GridBagConstraints.WEST;
        gbc1.fill = GridBagConstraints.HORIZONTAL;

        gbc1.gridx = 0;
        gbc1.gridy = 0;
        gbc1.weightx = 1.0; // Este peso absorbe el espacio sobrante a la izquierda
        panelInferior.add(Box.createHorizontalGlue(), gbc1);

        gbc1.gridx = 1;
        gbc1.gridy = 0;
        gbc1.weightx = 0;
        panelInferior.add(lblImporte,gbc1);

        gbc1.gridx = 2;
        gbc1.gridy = 0;
        gbc1.weightx = 0;
        panelInferior.add(txtPrecio,gbc1);

        gbc1.gridx = 1;
        gbc1.gridy = 1;
        gbc1.weightx = 0;
        panelInferior.add(btnSeguir,gbc1);

        gbc1.gridx = 2;
        gbc1.gridy = 1;
        gbc1.weightx = 0;
        panelInferior.add(btnProcesar,gbc1);

        panelPrincipal.add(panelSuperior,BorderLayout.NORTH);
        panelPrincipal.add(tablaSalida,BorderLayout.CENTER);
        panelPrincipal.add(panelInferior,BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
    }
}
