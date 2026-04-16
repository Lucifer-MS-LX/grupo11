package com.shoecenter.config;

import java.util.Locale;

public final class AppConfig {

    public static final String APP_TITLE = "SHOECENTER";
    public static final String IMAGE_FOLDER = "/images/";
    public static final String GENERO_HOMBRE = "Hombre";
    public static final String GENERO_MUJER = "Mujer";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_VENDEDOR = "VENDEDOR";
    public static final Locale APP_LOCALE = Locale.forLanguageTag("es-PE");

    private AppConfig() {
    }
}
