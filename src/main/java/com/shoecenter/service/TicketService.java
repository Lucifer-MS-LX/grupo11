package com.shoecenter.service;

import javax.swing.table.DefaultTableModel;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Date;

public class TicketService {
    public void generarTicket(DefaultTableModel modelo, String total, String metodoPago, String rutaCompleta) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaCompleta))) {
            writer.println("==========================================");
            writer.println("              SHOE CENTER                 ");
            writer.println("==========================================");
            writer.println("Fecha: " + new Date());
            writer.println("Método de Pago: " + metodoPago);
            writer.println("------------------------------------------");
            writer.println(String.format("%-20s %-10s %-10s", "Producto", "Cant.", "Subtotal"));

            for (int i = 0; i < modelo.getRowCount(); i++) {
                String desc = modelo.getValueAt(i, 1).toString();
                String cant = modelo.getValueAt(i, 0).toString();
                String sub = modelo.getValueAt(i, 3).toString();

                if (desc.length() > 19) desc = desc.substring(0, 17) + "..";
                writer.println(String.format("%-20s %-10s S/ %-10s", desc, cant, sub));
            }

            writer.println("------------------------------------------");
            writer.println("TOTAL A PAGAR:           S/ " + total);
            writer.println("==========================================");
            writer.println("      ¡Gracias por su compra!             ");
        }
    }
}