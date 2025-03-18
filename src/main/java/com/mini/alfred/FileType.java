package com.mini.alfred;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lion
 * @date 2025/3/18 23:14
 */
public class FileType {
    private final String value;
    private static final Map<String, String> ICON_MAP = new HashMap<>();

    static {
        ICON_MAP.put("pdf", "dict-icon");
        Arrays.asList("jpg", "png", "gif").forEach(x -> ICON_MAP.put(x, "alfred-icon"));
        Arrays.asList("doc", "docx", "txt").forEach(x -> ICON_MAP.put(x, "wiki-icon"));
    }

    public FileType(String fileName) {
        this.value = fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public String getIconClass() {
        return ICON_MAP.getOrDefault(value, "wiki-icon");
    }

}
