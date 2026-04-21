package com.shoecenter.app;
import com.shoecenter.ui.*;

import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
                configureSystemLookAndFeel();
                LoginForm form = new LoginForm();
                form.setVisible(true);
                });
            }

            public static void configureSystemLookAndFeel(){
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
               } catch(Exception e){
                System.err.println("No se puede aplicar la apariencia del sistema operativo.");
            }
        }

}
