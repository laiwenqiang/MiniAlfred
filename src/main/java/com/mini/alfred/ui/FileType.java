package com.mini.alfred.ui;

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
    private static final Map<String, String> ICON_TEXT_MAP = new HashMap<>();

    static {
        // CSS 样式类映射
        ICON_MAP.put("pdf", "dict-icon");
        Arrays.asList("jpg", "png", "gif").forEach(x -> ICON_MAP.put(x, "alfred-icon"));
        Arrays.asList("doc", "docx", "txt").forEach(x -> ICON_MAP.put(x, "wiki-icon"));
        
        // 图标文本映射
        ICON_TEXT_MAP.put("pdf", "📄");
        Arrays.asList("jpg", "png", "gif").forEach(x -> ICON_TEXT_MAP.put(x, "🖼"));
        Arrays.asList("doc", "docx", "txt").forEach(x -> ICON_TEXT_MAP.put(x, "📝"));
        Arrays.asList("mp3", "wav").forEach(x -> ICON_TEXT_MAP.put(x, "🎵"));
        Arrays.asList("mp4", "avi", "mov").forEach(x -> ICON_TEXT_MAP.put(x, "🎬"));
    }

    public FileType(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        this.value = dotIndex > 0 ? fileName.substring(dotIndex + 1).toLowerCase() : "";
    }

    public String getIconClass() {
        return ICON_MAP.getOrDefault(value, "wiki-icon");
    }
    
    public String getIconText() {
        return ICON_TEXT_MAP.getOrDefault(value, "📁");
    }
}
