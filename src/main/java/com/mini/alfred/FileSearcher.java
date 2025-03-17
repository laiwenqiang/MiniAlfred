package com.mini.alfred;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class FileSearcher {
    private final String searchPath;
    
    public FileSearcher() {
        this.searchPath = ConfigManager.getSearchPath();
    }
    
    public CompletableFuture<List<File>> searchAsync(String query) {
        return CompletableFuture.supplyAsync(() -> search(query));
    }
    
    public List<File> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String normalizedQuery = query.toLowerCase().trim();
        File directory = new File(searchPath);
        
        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("搜索路径不存在或不是目录: " + searchPath);
            return new ArrayList<>();
        }
        
        List<File> results = new ArrayList<>();
        searchInDirectory(directory, normalizedQuery, results);
        
        // 按照相关性排序（文件名包含查询字符串的排在前面）
        return results.stream()
            .sorted((f1, f2) -> {
                boolean f1ContainsQuery = f1.getName().toLowerCase().contains(normalizedQuery);
                boolean f2ContainsQuery = f2.getName().toLowerCase().contains(normalizedQuery);
                
                if (f1ContainsQuery && !f2ContainsQuery) return -1;
                if (!f1ContainsQuery && f2ContainsQuery) return 1;
                return f1.getName().compareTo(f2.getName());
            })
            .collect(Collectors.toList());
    }
    
    private void searchInDirectory(File directory, String query, List<File> results) {
        File[] files = directory.listFiles();
        if (files == null) return;
        
        for (File file : files) {
            if (file.isDirectory()) {
                searchInDirectory(file, query, results);
            } else {
                if (file.getName().toLowerCase().contains(query)) {
                    results.add(file);
                }
            }
        }
    }
} 