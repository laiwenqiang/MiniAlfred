package com.filesearch.core.model;

/**
 * @author Lion
 * @date 2025/2/2 22:28
 */
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.nio.file.Path;

public class SearchResult {
    private final Path filePath;
    private String displayTitle;

    public SearchResult(Path filePath) {
        this.filePath = filePath;
        this.displayTitle = generateDisplayTitle();
    }

    private String generateDisplayTitle() {
        return filePath.getFileName().toString();
    }

    public Path getFilePath() {
        return filePath;
    }

    public String getDisplayTitle() {
        return displayTitle;
    }

    public Icon getFileIcon() {
        return FileSystemView.getFileSystemView().getSystemIcon(filePath.toFile());
    }

    public void highlightMatches(String query) {
        String fileName = filePath.getFileName().toString();
        this.displayTitle = fileName.replaceAll("(?i)(" + query + ")", "<font color='red'>$1</font>");
    }

    @Override
    public String toString() {
        return "<html>" + displayTitle + "<br/><small>" + filePath + "</small></html>";
    }
}