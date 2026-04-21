package com.shoecenter.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mariadb://localhost:3306/shoecenter";
    private static final String USER = "admin_shoecenter";
    private static final String PASS = "heMiRPsaD92RS1gX"; // Déjalo como "" si no tienes clave

    public static Connection getConexion() {
        Connection con = null;
        try {
            // El driver que agregaste al pom.xml hace el trabajo aquí
            con = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Conexión exitosa a MariaDB");
        } catch (SQLException e) {
            System.err.println("Error al conectar: " + e.getMessage());
        }
        return con;
    }
}