package com.shoecenter.ui;

import com.shoecenter.model.Cliente;
import com.shoecenter.model.Usuario;
import com.shoecenter.repository.ClientesDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PuntodeVentaForm extends JDialog {

    private JLabel lblVendedor,lblCliente, lblImporte, lblAddress, lblPhone, lblPK;
    private JTextField txtVendedor, txtPrecio, txtAddress, txtPhone, txtPK, txtCliente;
    private JButton btnProcesar, btnSeguir, btnUpdate;
    private JTable tablaSalida;
    private DefaultTableModel modeloCarrito;

    private ClientesDAO clienteDAO;
    private Usuario usuarioSesion;


    public PuntodeVentaForm(Frame owner, ClientesDAO dao, DefaultTableModel modelo, Usuario usuario) {
        super(owner, "PUNTO DE VENTA", false);
        this.clienteDAO = dao;
        this.modeloCarrito = modelo;
        this.usuarioSesion = usuario;
        initUI();
        if (txtVendedor != null && usuarioSesion != null) {
            txtVendedor.setText(usuarioSesion.getUsername());
            txtVendedor.setEditable(false);
        }
        if (tablaSalida != null) {
            tablaSalida.setModel(this.modeloCarrito);
            actualizarTotal();
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
        setSize(750, 500);
        setLocationRelativeTo(getOwner());
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
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
        txtCliente = new JTextField(12);
        txtCliente.setEditable(false);

        tablaSalida = new JTable();
        tablaSalida.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        btnSeguir = new JButton("Continuar Comprando");
        btnProcesar = new JButton("Proceder a Pagar");
        btnUpdate = new JButton("Actualizar Cliente");
    }

    private void configureLayout(){
        JPanel panelPrincipal = new JPanel( new BorderLayout(10,12));
        panelPrincipal.setBorder(new EmptyBorder(15,20,15,20));

        JScrollPane scrollTabla = new JScrollPane(tablaSalida);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(null, "Pedido",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("Tahoma", Font.BOLD, 13)));


        JPanel panelSuperior = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(6,0,6,10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0;

        gbc.gridx = 0;
        gbc.weightx = 0;
        panelSuperior.add(lblVendedor,gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panelSuperior.add(txtVendedor,gbc);
        gbc.weightx = 0;
        gbc.gridx = 2;
        panelSuperior.add(lblPK,gbc);
        gbc.gridx = 3;
        panelSuperior.add(txtPK,gbc);


        gbc.gridy = 1;

        gbc.gridx = 0;
        gbc.weightx = 0;
        panelSuperior.add(lblCliente,gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panelSuperior.add(txtCliente,gbc);
        gbc.weightx = 0;
        gbc.gridx = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelSuperior.add(btnUpdate,gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2;

        gbc.gridx = 0;
        panelSuperior.add(lblAddress,gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panelSuperior.add(txtAddress,gbc);
        gbc.gridx = 2;
        gbc.weightx = 0;
        panelSuperior.add(lblPhone,gbc);
        gbc.gridx = 3;
        panelSuperior.add(txtPhone,gbc);

        JPanel panelInferior = new JPanel(new GridBagLayout());
        GridBagConstraints gbc1 = new GridBagConstraints();

        gbc1.insets = new Insets(6,0,6,10);
        gbc1.anchor = GridBagConstraints.WEST;
        gbc1.fill = GridBagConstraints.HORIZONTAL;

        gbc1.gridy = 0;
        gbc1.gridx = 0;
        gbc1.weightx = 1.0; // Este peso absorbe el espacio sobrante a la izquierda
        panelInferior.add(Box.createHorizontalGlue(), gbc1);

        gbc1.gridx = 1;
        gbc1.weightx = 0;
        panelInferior.add(lblImporte,gbc1);
        gbc1.gridx = 2;
        panelInferior.add(txtPrecio,gbc1);

        gbc1.gridy = 1;
        gbc1.gridx = 1;
        panelInferior.add(btnSeguir,gbc1);
        gbc1.gridx = 2;
        panelInferior.add(btnProcesar,gbc1);

        panelPrincipal.add(panelSuperior,BorderLayout.NORTH);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);
        //panelPrincipal.add(tablaSalida,BorderLayout.CENTER);
        panelPrincipal.add(panelInferior,BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
    }

    private void setupEvents(){
        txtPK.addActionListener(e -> buscarClienteAccion());
        btnUpdate.addActionListener(e -> actualizarClienteAccion());
        btnSeguir.addActionListener(e -> { this.setVisible(false); });
        btnProcesar.addActionListener(e -> procesarPagoAccion());

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem itemEliminar = new JMenuItem("Eliminar Producto del Pedido");

        itemEliminar.addActionListener(e -> eliminarProductoAccion());
        popupMenu.add(itemEliminar);

        tablaSalida.setComponentPopupMenu(popupMenu);
    }
    private void eliminarProductoAccion() {
        // Obtenemos el índice de la fila seleccionada
        int filaSeleccionada = tablaSalida.getSelectedRow();

        // Verificamos que realmente se haya seleccionado algo
        if (filaSeleccionada != -1) {
            int respuesta = JOptionPane.showConfirmDialog(this,
                    "¿Desea quitar este producto del pedido?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);

            if (respuesta == JOptionPane.YES_OPTION) {
                // Eliminamos la fila del modelo que compartes con la ventana principal
                modeloCarrito.removeRow(filaSeleccionada);

                // Usamos tu método existente para que el txtPrecio se actualice
                actualizarTotal();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla para eliminar.");
        }
    }

    private void buscarClienteAccion() {
        String documento = txtPK.getText().trim();

        if (documento.isEmpty()) {
            limpiarCamposCliente();
            return;
        }

        Cliente c = clienteDAO.obtenerClientePorDocumento(documento);
        if (c != null) {
            txtCliente.setText(c.getNombre());
            txtAddress.setText(c.getDireccion());
            txtPhone.setText(c.getTelefono());
        } else {
            limpiarCamposCliente();
            int op = JOptionPane.showConfirmDialog(this,
                    "El cliente no existe. ¿Desea registrarlo?", "Cliente no encontrado",
                    JOptionPane.YES_NO_OPTION);

            if (op == JOptionPane.YES_OPTION) {
                adicionarClienteAccion();
            }
        }
    }

    private void adicionarClienteAccion() {
        Cliente nuevo = new Cliente("", "", "", txtPK.getText().trim());
        new ClientForm(this, clienteDAO, nuevo, this::buscarClienteAccion).setVisible(true);
    }

    private void actualizarClienteAccion(){
        String documento = txtPK.getText().trim();
        Cliente c = clienteDAO.obtenerClientePorDocumento(documento);

        if (c != null) {
            // El formulario recibirá el objeto con datos y bloqueará el campo DNI/RUC
            new ClientForm(this, clienteDAO, c, this::buscarClienteAccion).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Primero debe buscar un cliente registrado.");
        }
    }

    private void procesarPagoAccion() {
        if (modeloCarrito.getRowCount() <= 0) {
            JOptionPane.showMessageDialog(this, "El carrito de compras está vacío.");
            return;
        }

        if (txtPK.getText().trim().isEmpty() || txtCliente.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe identificar a un cliente (DNI/RUC) antes de pagar.");
            txtPK.requestFocus();
            return;
        }

        String totalActual = txtPrecio.getText();
        PaymentForm ventanaPago = new PaymentForm(modeloCarrito, totalActual);
        ventanaPago.setVisible(true);

        this.dispose();
    }

    public void actualizarTotal() {
        double total = 0;
        for (int i = 0; i < modeloCarrito.getRowCount(); i++) {
            total += (double) modeloCarrito.getValueAt(i, 3);
        }
        txtPrecio.setText(String.valueOf(total));
    }

    private void limpiarCamposCliente() {
        txtCliente.setText("");
        txtAddress.setText("");
        txtPhone.setText("");
    }

}