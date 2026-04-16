package com.shoecenter.ui;

import com.shoecenter.model.Usuario;
import com.shoecenter.repository.UsuarioDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginForm extends JFrame {

    private JLabel lblUser, lblPassword, lblImagen;
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public LoginForm() {
        initLoginUI();
    }

    private void initLoginUI(){
        configureWindow();
        initializeComponents();
        configureLayout();
        setupEvents();
    }

    private void configureWindow(){
        setTitle("Login - ShoeCenter");
        setSize(300, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initializeComponents() {

        lblUser = new JLabel("Usuario");
        lblUser.setFont(new Font("Tahoma",Font.BOLD,12));
        lblUser.setHorizontalAlignment(SwingConstants.CENTER);

        lblPassword = new JLabel("Contrasenha");
        lblPassword.setFont(new Font("Tahoma",Font.BOLD,12));
        lblPassword.setHorizontalAlignment(SwingConstants.CENTER);

        ImageIcon icono = new ImageIcon("/home/jose/Cibertec/Idea_Apps/catalogo/src/main/resources/images/shoecenter.png");
        Image iconoEscalado = icono.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        lblImagen = new JLabel(new ImageIcon(iconoEscalado));
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);


        txtUser = new JTextField(12);
        txtPass = new JPasswordField();

        btnLogin = new JButton("Log in");
    }

    private void configureLayout(){

        JPanel panelPrincipal = new JPanel( new BorderLayout(10,12));
        panelPrincipal.setBorder(new EmptyBorder(15,20,15,20));

        JPanel panelCentral = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(6,0,6,10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panelCentral.add(lblUser,gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        panelCentral.add(txtUser,gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panelCentral.add(lblPassword,gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        panelCentral.add(txtPass,gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1;
        panelCentral.add(btnLogin,gbc);

        panelPrincipal.add(lblImagen,BorderLayout.NORTH);
        panelPrincipal.add(panelCentral,BorderLayout.CENTER);

        setContentPane(panelPrincipal);
    }

    private void setupEvents(){
        btnLogin.addActionListener(e -> validar());
        txtPass.addActionListener(e -> validar());
    }

    private void validar() {
            String user = txtUser.getText();
            String pass = new String(txtPass.getPassword());

            Usuario u = usuarioDAO.validarAcceso(user, pass);

            if (u != null) {
                new StarterForm(u).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos",
                        "Error de Acceso", JOptionPane.ERROR_MESSAGE);
            }
        }
}
