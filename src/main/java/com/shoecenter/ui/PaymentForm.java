package com.shoecenter.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PaymentForm extends JFrame {

    // Componentes de Selección (Izquierda)
    private JRadioButton rbEnTienda, rbDelivery, rbEfectivo, rbYape;
    private ButtonGroup grupoEnvio, grupoPago;
    private JPanel panelDinamicoEnvio, panelDinamicoPago;
    private JComboBox<String> cbTiendas;
    private JTextField txtDireccion;
    private JLabel lblDinamicoEnvio, lblImagenQR, lblTotal;

    // Componentes de Vista Previa (Derecha)
    private JTable tblPreview;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollTabla;

    // Datos que vienen del Punto de Venta
    private DefaultTableModel modeloCarrito;
    private String totalVenta;

    public PaymentForm(DefaultTableModel modelo, String totalVenta) {
        this.modeloCarrito = modelo;
        this.totalVenta = totalVenta;
        initUI();
        registerEvent();
        mostrarResumen();
    }

    private void initUI() {
        configureWindow();
        initializaComponents();
        configureLayouts();
    }

    private void configureWindow() {
        setTitle("Finalizar Pedido - Shoe Center");
        setSize(950, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initializaComponents() {
        Font fontTahoma = new Font("Tahoma", Font.PLAIN, 13);

        // Radios
        rbEnTienda = new JRadioButton("En tienda", true);
        rbDelivery = new JRadioButton("Delivery");
        grupoEnvio = new ButtonGroup();
        grupoEnvio.add(rbEnTienda); grupoEnvio.add(rbDelivery);

        rbEfectivo = new JRadioButton("Efectivo", true);
        rbYape = new JRadioButton("Yape");
        grupoPago = new ButtonGroup();
        grupoPago.add(rbEfectivo); grupoPago.add(rbYape);

        // Dinámicos
        cbTiendas = new JComboBox<>(new String[]{"Tienda Central", "Sucursal A"});
        txtDireccion = new JTextField(20);
        lblDinamicoEnvio = new JLabel("Tienda :");

        lblImagenQR = new JLabel("[ QR Yape ]", SwingConstants.CENTER);
        lblImagenQR.setPreferredSize(new Dimension(150, 150));
        lblImagenQR.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Tabla: Definimos las 4 columnas exactas que vienen del carrito
        String[] columnas = {"Producto", "Cantidad", "Precio", "Subtotal"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblPreview = new JTable(modeloTabla);
        scrollTabla = new JScrollPane(tblPreview);

        // Label del Total: Usamos directamente el String que recibimos
        lblTotal = new JLabel("TOTAL A PAGAR: " + totalVenta, SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTotal.setForeground(new Color(0, 102, 51));
    }

    private void mostrarResumen() {
        modeloTabla.setRowCount(0);
        // Traspaso directo de las 4 columnas sin cálculos adicionales
        for (int i = 0; i < modeloCarrito.getRowCount(); i++) {
            modeloTabla.addRow(new Object[]{
                    modeloCarrito.getValueAt(i, 0), // Producto
                    modeloCarrito.getValueAt(i, 1), // Cantidad
                    modeloCarrito.getValueAt(i, 2), // Precio
                    modeloCarrito.getValueAt(i, 3)  // Subtotal (Ya calculado en la otra ventana)
            });
        }
    }

    private void configureLayouts() {
        JPanel contentPrincipal = new JPanel(new BorderLayout(15, 0));
        contentPrincipal.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Formulario (Izquierda)
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setPreferredSize(new Dimension(400, 450));

        JPanel pEnvio = crearPanelBordeado("Modo de Envío :");
        pEnvio.add(rbEnTienda); pEnvio.add(rbDelivery);

        panelDinamicoEnvio = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JPanel pPago = crearPanelBordeado("Modo de Pago :");
        pPago.add(rbEfectivo); pPago.add(rbYape);

        panelDinamicoPago = new JPanel(new BorderLayout());
        panelDinamicoPago.setBorder(new EmptyBorder(5, 50, 5, 50));

        panelIzquierdo.add(pEnvio);
        panelIzquierdo.add(panelDinamicoEnvio);
        panelIzquierdo.add(Box.createVerticalStrut(15));
        panelIzquierdo.add(pPago);
        panelIzquierdo.add(panelDinamicoPago);

        // Resumen (Derecha)
        JPanel panelDerecho = new JPanel(new BorderLayout(0, 10));
        panelDerecho.setBorder(BorderFactory.createTitledBorder(null, "Resumen del Pedido",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.BOLD, 13)));

        panelDerecho.add(scrollTabla, BorderLayout.CENTER);
        panelDerecho.add(lblTotal, BorderLayout.SOUTH);

        contentPrincipal.add(panelIzquierdo, BorderLayout.WEST);
        contentPrincipal.add(panelDerecho, BorderLayout.CENTER);

        actualizarEnvio();
        actualizarPago();
        setContentPane(contentPrincipal);
    }

    private JPanel crearPanelBordeado(String titulo) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        p.setBorder(BorderFactory.createTitledBorder(null, titulo, 0, 0, new Font("Tahoma", Font.BOLD, 13)));
        return p;
    }

    private void registerEvent() {
        rbEnTienda.addActionListener(e -> actualizarEnvio());
        rbDelivery.addActionListener(e -> actualizarEnvio());
        rbEfectivo.addActionListener(e -> actualizarPago());
        rbYape.addActionListener(e -> actualizarPago());
    }

    private void actualizarEnvio() {
        panelDinamicoEnvio.removeAll();
        if (rbEnTienda.isSelected()) {
            panelDinamicoEnvio.add(lblDinamicoEnvio);
            panelDinamicoEnvio.add(cbTiendas);
        } else {
            panelDinamicoEnvio.add(new JLabel("Dirección: "));
            panelDinamicoEnvio.add(txtDireccion);
        }
        panelDinamicoEnvio.revalidate(); panelDinamicoEnvio.repaint();
    }

    private void actualizarPago() {
        panelDinamicoPago.removeAll();
        if (rbYape.isSelected()) panelDinamicoPago.add(lblImagenQR, BorderLayout.CENTER);
        panelDinamicoPago.revalidate(); panelDinamicoPago.repaint();
    }
}