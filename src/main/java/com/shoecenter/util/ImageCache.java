package com.shoecenter.util;

import com.shoecenter.config.AppConfig;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ImageCache {

    private static final Map<String, ImageIcon> CACHE = new ConcurrentHashMap<>();

    private ImageCache() {
    }

    public static ImageIcon load(String fileName, int width, int height) {
        String cacheKey = fileName + ":" + width + "x" + height;
        return CACHE.computeIfAbsent(cacheKey, key -> createIcon(fileName, width, height));
    }

    private static ImageIcon createIcon(String fileName, int width, int height) {
        URL url = ImageCache.class.getResource(AppConfig.IMAGE_FOLDER + fileName);
        if (url == null) {
            return null;
        }
        ImageIcon icon = new ImageIcon(url);
        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
}
