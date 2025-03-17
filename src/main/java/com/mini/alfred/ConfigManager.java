package com.mini.alfred;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static Properties properties;
    private static final String CONFIG_FILE = "/config.properties";
    
    static {
        properties = new Properties();
        try (InputStream input = ConfigManager.class.getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
            } else {
                System.err.println("无法找到配置文件: " + CONFIG_FILE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String getSearchPath() {
        return properties.getProperty("search.path", "");
    }
} 