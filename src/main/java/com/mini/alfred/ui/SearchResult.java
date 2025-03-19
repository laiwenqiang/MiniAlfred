package com.mini.alfred.ui;

import java.io.File;

/**
 * 封装搜索结果的类
 */
public class SearchResult {
    private final File file;
    private final FileType fileType;
    private final String displayName;
    private final String path;

    public SearchResult(File file) {
        this.file = file;
        this.fileType = new FileType(file.getName());
        this.displayName = file.getName();
        this.path = file.getParentFile().getAbsolutePath();
    }

    public File getFile() {
        return file;
    }

    public FileType getFileType() {
        return fileType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPath() {
        return path;
    }
}