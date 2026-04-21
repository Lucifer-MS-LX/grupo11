package com.shoecenter.ui;
import com.shoecenter.config.config;
import com.shoecenter.model.Producto;
import com.shoecenter.model.Usuario;
import com.shoecenter.repository.ProductoDAO;
import com.shoecenter.service.NavigationService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.awt.*;

public class StarterForm extends JFrame {

    private JRadioButton rbman, rbwoman;
    private ButtonGroup groupGenero;
    private JPanel panelFotos;

    private DefaultTableModel modeloCarrito;
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final Usuario usuarioSesion;
    private final boolean isAdmin;
    private final NavigationService navigationService;

    public StarterForm(Usuario usuario) {
        this.usuarioSesion = usuario;
        this.navigationService = new NavigationService(this);
        this.isAdmin = usuario.getRol().equalsIgnoreCase("ADMIN");
        initUI();
    }

    private void initUI() {
        configureWindow();
        if (isAdmin) {
            configureMenuBar();
        }
        initializeComponents();
        setUpEvents();
        configureLayout();
        generarBotonesDinamicos("Hombre");
    }

    private void configureWindow() {
        setTitle("SHOECENTER" + " - " + usuarioSesion.getUsername());
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {

        rbman = new JRadioButton("Hombre", true);
        rbwoman = new JRadioButton("Mujer", false);
        groupGenero = new ButtonGroup();
        groupGenero.add(rbman); groupGenero.add(rbwoman);

        panelFotos = new JPanel(new GridLayout(0, 3, 15, 15));
        panelFotos.setBorder(new EmptyBorder(15, 15, 15, 15));

        String[] columnas = {"Cantidad", "Descripción", "P.U.", "Importe"};
        modeloCarrito = new DefaultTableModel(columnas, 0);
    }

    private void setUpEvents() {
        rbman.addActionListener(e -> generarBotonesDinamicos("Hombre"));
        rbwoman.addActionListener(e -> generarBotonesDinamicos("Mujer"));
    }

    private void configureLayout() {
        JPanel main = new JPanel(new BorderLayout());

        JPanel pNorth = new JPanel();
        pNorth.add(new JLabel("Filtrar por: "));
        pNorth.add(rbman); pNorth.add(rbwoman);

        main.add(pNorth, BorderLayout.NORTH);
        main.add(new JScrollPane(panelFotos), BorderLayout.CENTER);

        setContentPane(main);
    }

    private void generarBotonesDinamicos(String genero) {
        panelFotos.removeAll();
        List<Producto> lista = productoDAO.buscarPorGenero(genero);

        for (Producto p : lista) {
            JButton btn = crearBotonProducto(p);
            panelFotos.add(btn);
        }

        panelFotos.revalidate();
        panelFotos.repaint();
    }

    private JButton crearBotonProducto(Producto p) {
        String html = "<html><center><b>" + p.getMarca() + "</b><br>S/ " + p.getPrecio() + "</center></html>";

        ImageIcon icon = new ImageIcon(config.PATH + p.getFoto());
        Image img = icon.getImage().getScaledInstance(140, 80, Image.SCALE_SMOOTH);

        JButton btn = new JButton(html, new ImageIcon(img));
        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.setBackground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            new DetalleProductoForm(this, p, usuarioSesion, modeloCarrito).setVisible(true);
        });
        return btn;
    }

    // En caso somos administradores tenemos que lidiar con la barra y ver sus accion.
    private void configureMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuAccion = new JMenu("Acciones");
        JMenuItem itemAdd = new JMenuItem("Nuevo Producto");

        itemAdd.addActionListener(e -> abrirFormularioNuevoProducto());

        menuAccion.add(itemAdd);
        menuBar.add(menuAccion);

        setJMenuBar(menuBar);
    }

    private void abrirFormularioNuevoProducto(){
        new AddProducto(this).setVisible(true);

        String generoActual = rbman.isSelected() ? "Hombre" : "Mujer";
        generarBotonesDinamicos(generoActual);
    }


    // Funciones para gestionar
    public PuntodeVentaForm getPuntodeVenta() { return navigationService.getPuntodeVenta(modeloCarrito,usuarioSesion);}

    public void resetearPuntoDeVenta() { navigationService.resetear();}
}