package com.shoecenter.app;

import com.shoecenter.repository.ClientesDAO;
import com.shoecenter.ui.*;

import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            LoginForm form = new LoginForm();
            //AddProducto form = new AddProducto();
            //ClientesDAO dao = new ClientesDAO();
            //PuntodeVentaForm form = new PuntodeVentaForm(dao);
            //PaymentForm form = new PaymentForm();
            form.setVisible(true);
        });
    }
}
