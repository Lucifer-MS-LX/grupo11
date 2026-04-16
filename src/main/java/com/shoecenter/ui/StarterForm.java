package com.shoecenter.ui;

import com.shoecenter.config.config;
import com.shoecenter.model.Producto;
import com.shoecenter.model.Usuario;
import com.shoecenter.repository.ProductoDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.awt.*;

public class StarterForm extends JFrame {

    private JMenuBar menuBar;
    private JMenu menuAccion;
    private JRadioButton rbman, rbwoman;
    private ButtonGroup groupGenero;
    private JPanel panelFotos;

    private DefaultTableModel modeloCarrito;
    private final ProductoDAO productoDAO = new ProductoDAO();
    private Usuario usuarioSesion;

    public StarterForm(Usuario usuario) {
        this.usuarioSesion = usuario;
        initUI();
    }

    private void initUI() {
        configureWindow();
        initializeComponents();
        setUpEvents();
        configureLayout();
        generarBotonesDinamicos(config.TEXT_RBMAN);
    }

    private void configureWindow() {
        setTitle(config.TEXT_TITLE + " - " + usuarioSesion.getUsername());
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        menuBar = new JMenuBar();
        menuAccion = new JMenu("Acciones");

        JMenuItem itemAdd = new JMenuItem("Nuevo Producto");
        menuAccion.add(itemAdd);

        setJMenuBar(menuBar);
        menuBar.add(menuAccion);

        if (!usuarioSesion.getRol().equalsIgnoreCase("ADMIN")) {
            menuAccion.setVisible(false);
        }

        rbman = new JRadioButton(config.TEXT_RBMAN, true);
        rbwoman = new JRadioButton(config.TEXT_RBWOMAN, false);
        groupGenero = new ButtonGroup();
        groupGenero.add(rbman); groupGenero.add(rbwoman);

        panelFotos = new JPanel(new GridLayout(0, 3, 15, 15));
        panelFotos.setBorder(new EmptyBorder(15, 15, 15, 15));

        String[] columnas = {"Cantidad", "Descripción", "P.U.", "Importe"};
        modeloCarrito = new DefaultTableModel(columnas, 0);
    }

    private void setUpEvents() {
        Component[] menuItems = menuAccion.getMenuComponents();
        for (Component item : menuItems) {
            if (item instanceof JMenuItem && ((JMenuItem) item).getText().equals("Nuevo Producto")) {
                ((JMenuItem) item).addActionListener(e -> {
                    new AddProducto().setVisible(true);
                });
            }
        }

        rbman.addActionListener(e -> generarBotonesDinamicos(config.TEXT_RBMAN));
        rbwoman.addActionListener(e -> generarBotonesDinamicos(config.TEXT_RBWOMAN));
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
            panelFotos.add(btn); // adicionando los botones al panel Fotos
        }

        panelFotos.revalidate(); // ubicando los botones en el panel Fotos
        panelFotos.repaint(); // mostrando los botones en el panel Fotos
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
            boolean isAdmin = usuarioSesion.getRol().equalsIgnoreCase("ADMIN");
            new DetalleProductoForm(this, p, isAdmin, modeloCarrito).setVisible(true);
        });

        return btn;
    }
}