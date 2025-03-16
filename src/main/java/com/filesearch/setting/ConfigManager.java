package com.filesearch.setting;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * @author Lion
 * @date 2025/2/2 15:05
 */
public class ConfigManager {
    private static final String CONFIG_FILE = "config.properties";
    private Properties props = new Properties();
    private static ConfigManager instance = null;

    private ConfigManager() {
        initDefaultConfig();
        loadConfig();
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    public String getString(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public void set(String key, String value) throws IOException {
        props.setProperty(key, value);
        props.store(new FileOutputStream(CONFIG_FILE), null);
    }

    private void loadConfig() {
        try {
            InputStream input = this.getClass().getClassLoader().getResourceAsStream(CONFIG_FILE);
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initDefaultConfig() {
        props.setProperty("search.path", System.getProperty("user.home"));
        props.setProperty("hotkey.show", "ALT+SPACE");
        props.setProperty("hotkey.hide", "ESC");
    }

    public Path getSearchPath() {
        return Paths.get(props.getProperty("search.path"));
    }

}