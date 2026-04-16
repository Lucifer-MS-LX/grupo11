package com.shoecenter.util;

import com.shoecenter.config.AppConfig;

import java.math.BigDecimal;
import java.text.NumberFormat;

public final class CurrencyFormatter {

    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(AppConfig.APP_LOCALE);

    private CurrencyFormatter() {
    }

    public static String format(BigDecimal amount) {
        return CURRENCY_FORMAT.format(amount);
    }
}
