package com.mini.alfred;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class AlfredController implements Initializable {

    @FXML
    private TextField searchInput;
    
    @FXML
    private VBox resultsContainer;
    
    private List<HBox> resultItems = new ArrayList<>();
    private int selectedIndex = 0;
    private FileSearcher fileSearcher;
    private List<File> searchResults = new ArrayList<>();
    private CompletableFuture<Void> searchTask;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileSearcher = new FileSearcher();
        
        // 设置搜索输入监听
        searchInput.textProperty().addListener((observable, oldValue, newValue) -> {
            performSearch(newValue);
        });
        
        // 键盘导航监听
        searchInput.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DOWN) {
                selectNextItem();
                event.consume();
            } else if (event.getCode() == KeyCode.UP) {
                selectPreviousItem();
                event.consume();
            } else if (event.getCode() == KeyCode.ENTER) {
                handleSelection();
                event.consume();
            }
        });
        
        // 初始搜索
        performSearch(searchInput.getText());
    }
    
    private void performSearch(String query) {
        // 取消之前的搜索任务
        if (searchTask != null && !searchTask.isDone()) {
            searchTask.cancel(true);
        }
        
        // 开始新的搜索任务
        searchTask = fileSearcher.searchAsync(query)
            .thenAccept(files -> {
                Platform.runLater(() -> {
                    searchResults = files;
                    updateSearchResults();
                });
            });
    }
    
    private void updateSearchResults() {
        resultsContainer.getChildren().clear();
        resultItems.clear();
        
        if (searchResults.isEmpty()) {
            addNoResultsItem();
        } else {
            for (File file : searchResults) {
                addFileResultItem(file);
            }
        }
        
        // 选中第一项
        selectedIndex = 0;
        updateSelection();
    }
    
    private void addNoResultsItem() {
        HBox resultItem = new HBox();
        resultItem.getStyleClass().add("result-item");
        
        // 图标
        StackPane iconContainer = new StackPane();
        iconContainer.getStyleClass().addAll("result-icon", "alfred-icon");
        Label iconLabel = new Label("🔍");
        iconContainer.getChildren().add(iconLabel);
        
        // 文本
        VBox textContainer = new VBox();
        textContainer.getStyleClass().add("result-text");
        HBox.setHgrow(textContainer, Priority.ALWAYS);
        Label titleLabel = new Label("未找到匹配的文件");
        titleLabel.getStyleClass().add("result-title");
        textContainer.getChildren().add(titleLabel);
        
        // 组合元素
        resultItem.getChildren().addAll(iconContainer, textContainer);
        
        // 添加到结果容器和列表
        resultsContainer.getChildren().add(resultItem);
        resultItems.add(resultItem);
    }
    
    private void addFileResultItem(File file) {
        // 创建结果项容器
        HBox resultItem = new HBox();
        resultItem.getStyleClass().add("result-item");
        
        // 图标 (根据文件类型选择不同图标)
        StackPane iconContainer = new StackPane();
        String iconClass = getFileIconClass(file);
        iconContainer.getStyleClass().addAll("result-icon", iconClass);
        Label iconLabel = new Label(getFileIconText(file));
        iconContainer.getChildren().add(iconLabel);
        
        // 文本
        VBox textContainer = new VBox();
        textContainer.getStyleClass().add("result-text");
        HBox.setHgrow(textContainer, Priority.ALWAYS);
        
        Label titleLabel = new Label(file.getName());
        titleLabel.getStyleClass().add("result-title");
        
        Label pathLabel = new Label(file.getParentFile().getAbsolutePath());
        pathLabel.getStyleClass().add("result-path");
        pathLabel.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 12px;");
        
        textContainer.getChildren().addAll(titleLabel, pathLabel);
        
        // 快捷键/操作
        Label shortcutLabel = new Label("Enter");
        shortcutLabel.getStyleClass().add("shortcut");
        
        // 组合所有元素
        resultItem.getChildren().addAll(iconContainer, textContainer, shortcutLabel);
        
        // 设置点击事件
        resultItem.setOnMouseClicked(event -> {
            handleItemClick(resultItem);
        });
        
        // 设置鼠标悬停事件
        resultItem.setOnMouseEntered(event -> {
            selectItem(resultItems.indexOf(resultItem));
        });
        
        // 添加到结果容器和列表
        resultsContainer.getChildren().add(resultItem);
        resultItems.add(resultItem);
    }
    
    private String getFileIconClass(File file) {
        String name = file.getName().toLowerCase();
        if (name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".gif")) {
            return "alfred-icon";  // 图片文件
        } else if (name.endsWith(".pdf")) {
            return "dict-icon";    // PDF文件
        } else if (name.endsWith(".doc") || name.endsWith(".docx") || name.endsWith(".txt")) {
            return "wiki-icon";    // 文档文件
        } else if (name.endsWith(".mp3") || name.endsWith(".wav")) {
            return "youtube-icon"; // 音频文件
        } else if (name.endsWith(".mp4") || name.endsWith(".avi") || name.endsWith(".mov")) {
            return "youtube-icon"; // 视频文件
        } else {
            return "google-icon";  // 其他文件
        }
    }
    
    private String getFileIconText(File file) {
        String name = file.getName().toLowerCase();
        if (name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".gif")) {
            return "🖼";
        } else if (name.endsWith(".pdf")) {
            return "📄";
        } else if (name.endsWith(".doc") || name.endsWith(".docx") || name.endsWith(".txt")) {
            return "📝";
        } else if (name.endsWith(".mp3") || name.endsWith(".wav")) {
            return "🎵";
        } else if (name.endsWith(".mp4") || name.endsWith(".avi") || name.endsWith(".mov")) {
            return "🎬";
        } else {
            return "📁";
        }
    }
    
    private void selectNextItem() {
        if (resultItems.isEmpty()) return;
        selectedIndex = (selectedIndex + 1) % resultItems.size();
        updateSelection();
    }
    
    private void selectPreviousItem() {
        if (resultItems.isEmpty()) return;
        selectedIndex = (selectedIndex - 1 + resultItems.size()) % resultItems.size();
        updateSelection();
    }
    
    private void selectItem(int index) {
        if (index >= 0 && index < resultItems.size()) {
            selectedIndex = index;
            updateSelection();
        }
    }
    
    private void updateSelection() {
        for (int i = 0; i < resultItems.size(); i++) {
            HBox item = resultItems.get(i);
            if (i == selectedIndex) {
                if (!item.getStyleClass().contains("selected")) {
                    item.getStyleClass().add("selected");
                }
            } else {
                item.getStyleClass().remove("selected");
            }
        }
    }
    
    private void handleItemClick(HBox item) {
        int index = resultItems.indexOf(item);
        if (index >= 0) {
            selectItem(index);
            handleSelection();
        }
    }
    
    private void handleSelection() {
        if (searchResults.isEmpty() || selectedIndex < 0 || selectedIndex >= searchResults.size()) {
            return;
        }
        
        File selectedFile = searchResults.get(selectedIndex);
        tryOpenFile(selectedFile);
    }
    
    private void tryOpenFile(File file) {
        try {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.OPEN)) {
                desktop.open(file);
            } else {
                showAlert("不支持打开文件操作", "您的系统不支持直接打开文件。");
            }
        } catch (IOException e) {
            showAlert("无法打开文件", "打开文件时出错: " + e.getMessage());
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 