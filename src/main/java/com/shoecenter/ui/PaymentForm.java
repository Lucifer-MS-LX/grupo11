package com.shoecenter.ui;

import com.shoecenter.service.TicketService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import javax.swing.JOptionPane;

public class PaymentForm extends JFrame {

    private JRadioButton rbEnTienda, rbDelivery, rbEfectivo, rbYape;
    private ButtonGroup grupoEnvio, grupoPago;
    private JPanel panelDinamicoEnvio, panelDinamicoPago;
    private JComboBox<String> cbTiendas;
    private JTextField txtDireccion;
    private JLabel lblDinamicoEnvio, lblImagenQR, lblTotal;

    private JTable tblPreview;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollTabla;

    private DefaultTableModel modeloCarrito;
    private String totalVenta;

    private final TicketService ticketService = new TicketService();
    private JButton btnConfirmar;

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

        btnConfirmar = new JButton("Confirmar Pago y Generar Ticket");
        btnConfirmar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnConfirmar.setBackground(new Color(40, 167, 69)); // Un verde elegante
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setCursor(new Cursor(Cursor.HAND_CURSOR));
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

        JPanel panelDerecho = new JPanel(new BorderLayout(0, 10));
        panelDerecho.setBorder(BorderFactory.createTitledBorder(null, "Resumen del Pedido",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.BOLD, 13)));

        panelDerecho.add(scrollTabla, BorderLayout.CENTER);

        JPanel panelAuxiliar = new JPanel(new GridLayout(1, 2, 5, 5));
        panelAuxiliar.setBorder(new EmptyBorder(10, 0, 0, 0)); // Espacio superior

        panelAuxiliar.add(lblTotal);      // Fila 1: El monto total
        panelAuxiliar.add(btnConfirmar);

        panelDerecho.add(panelAuxiliar, BorderLayout.SOUTH);

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

        btnConfirmar.addActionListener(e -> procesarConfirmacionPago());
    }

    private void procesarConfirmacionPago() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccione dónde guardar el ticket");

        // Sugerimos un nombre por defecto basado en el tiempo
        String nombreSugerido = "ticket_" + System.currentTimeMillis() + ".txt";
        chooser.setSelectedFile(new java.io.File(nombreSugerido));

        int seleccion = chooser.showSaveDialog(this);

        if (seleccion == 0){
            try {
                String rutaElegida = chooser.getSelectedFile().getAbsolutePath();
                String metodo = rbYape.isSelected() ? "YAPE" : "EFECTIVO";

                // Pasamos la ruta elegida al servicio
                ticketService.generarTicket(modeloCarrito, totalVenta, metodo, rutaElegida);

                JOptionPane.showMessageDialog(this, "Ticket guardado en: " + rutaElegida);

                this.dispose();
                // Aquí podrías llamar al reset del formulario principal si es necesario

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
            }
        }
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

        if (rbYape.isSelected()) {
            panelDinamicoPago.setLayout(new BorderLayout(5, 5));

            // QuickChart es el estándar actual para QRs sencillos por URL
            String contenido = "YAPE_PAGO_SHOE_CENTER_S/" + totalVenta;
            String urlQR = "https://quickchart.io/qr?text=" + contenido + "&size=250";

            try {
                // Imprimimos para que verifiques en tu terminal de Arch
                System.out.println("Generando QR dinámico en: " + urlQR);

                ImageIcon qrIcon = new ImageIcon(new java.net.URL(urlQR));
                lblImagenQR = new JLabel(qrIcon);
                lblImagenQR.setHorizontalAlignment(JLabel.CENTER);

                JLabel lblInstruccion = new JLabel("Escanea para pagar S/ " + totalVenta);
                lblInstruccion.setFont(new Font("Tahoma", Font.BOLD, 14));
                lblInstruccion.setForeground(new Color(113, 34, 131)); // Púrpura Yape
                lblInstruccion.setHorizontalAlignment(JLabel.CENTER);

                panelDinamicoPago.add(lblInstruccion, BorderLayout.NORTH);
                panelDinamicoPago.add(lblImagenQR, BorderLayout.CENTER);

            } catch (Exception e) {
                System.err.println("Error de red: " + e.getMessage());
                panelDinamicoPago.add(new JLabel("Error: Verifique su conexión a internet"), BorderLayout.CENTER);
            }
        } else if (rbEfectivo.isSelected()) {
            panelDinamicoPago.setLayout(new FlowLayout(FlowLayout.CENTER));
            panelDinamicoPago.add(new JLabel("Pague S/ " + totalVenta + " directamente en caja."));
        }

        panelDinamicoPago.revalidate();
        panelDinamicoPago.repaint();
    }
}
