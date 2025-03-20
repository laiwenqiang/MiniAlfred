package com.mini.alfred.ui;

import java.io.File;

public class SearchResult {
    private final String title;
    private final String subtitle;
    private final String iconClass;
    private final String iconText;
    private final ResultType type;
    private File file;

    public enum ResultType {
        FILE,
        CALCULATOR
    }
    
    // 文件结果构造函数
    public SearchResult(File file) {
        this.file = file;
        this.title = file.getName();
        this.subtitle = file.getParentFile().getAbsolutePath();
        FileType fileType = new FileType(file.getName());
        this.iconClass = fileType.getIconClass();
        this.iconText = fileType.getIconText();
        this.type = ResultType.FILE;
    }
    
    // 计算器结果构造函数
    public SearchResult(String expression, double result) {
        this.title = String.valueOf(result);
        this.subtitle = "";
        this.iconClass = "calculator-icon";
        this.iconText = "=";
        this.type = ResultType.CALCULATOR;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getSubtitle() {
        return subtitle;
    }
    
    public String getIconClass() {
        return iconClass;
    }
    
    public String getIconText() {
        return iconText;
    }
    
    public ResultType getType() {
        return type;
    }
    
    public File getFile() {
        return file;
    }
}