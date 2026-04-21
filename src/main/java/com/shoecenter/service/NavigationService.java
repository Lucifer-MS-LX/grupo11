package com.shoecenter.service;

import com.shoecenter.model.Usuario;
import com.shoecenter.repository.ClientesDAO;
import com.shoecenter.ui.PuntodeVentaForm;
import com.shoecenter.ui.StarterForm;

import javax.swing.table.DefaultTableModel;

public class NavigationService {
    private PuntodeVentaForm pdv = null;
    private final StarterForm parent;

    public NavigationService(StarterForm parent) {
        this.parent = parent;
    }

    public PuntodeVentaForm getPuntodeVenta(DefaultTableModel modelo, Usuario usuario) {
        if (pdv == null) {
            pdv = new PuntodeVentaForm(parent, new ClientesDAO(), modelo, usuario);
        }
        return pdv;
    }

    public void resetear() {
        pdv = null;
    }
}