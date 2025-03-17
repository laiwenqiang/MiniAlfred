package com.mini.alfred;

public class SearchResult {
    private String iconClass;
    private String iconText;
    private String title;
    private String shortcut;
    
    public SearchResult(String iconClass, String iconText, String title, String shortcut) {
        this.iconClass = iconClass;
        this.iconText = iconText;
        this.title = title;
        this.shortcut = shortcut;
    }
    
    public String getIconClass() {
        return iconClass;
    }
    
    public String getIconText() {
        return iconText;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getShortcut() {
        return shortcut;
    }
} 