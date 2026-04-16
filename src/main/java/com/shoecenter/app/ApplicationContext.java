package com.shoecenter.app;

import com.shoecenter.model.Usuario;
import com.shoecenter.repository.ClientesDAO;
import com.shoecenter.repository.ProductoDAO;
import com.shoecenter.repository.UsuarioDAO;
import com.shoecenter.service.AuthService;
import com.shoecenter.service.CartService;
import com.shoecenter.service.CustomerService;
import com.shoecenter.service.ProductService;

public class ApplicationContext {

    private final ProductService productService;
    private final CustomerService customerService;
    private final AuthService authService;
    private final CartService cartService;
    private Usuario currentUser;

    public ApplicationContext() {
        ProductoDAO productoDAO = new ProductoDAO();
        ClientesDAO clientesDAO = new ClientesDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        productService = new ProductService(productoDAO);
        customerService = new CustomerService(clientesDAO);
        authService = new AuthService(usuarioDAO);
        cartService = new CartService();
    }

    public ProductService getProductService() {
        return productService;
    }

    public CustomerService getCustomerService() {
        return customerService;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public CartService getCartService() {
        return cartService;
    }

    public Usuario getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Usuario currentUser) {
        this.currentUser = currentUser;
    }
}
