package com.mini.alfred.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class SearchResultItemFactory {
    
    public static HBox createNoResultItem() {
        HBox resultItem = new HBox();
        resultItem.getStyleClass().add("result-item");

        StackPane iconContainer = createIconContainer("🔍", "alfred-icon");
        VBox textContainer = createTextContainer("未找到匹配的文件", null);

        resultItem.getChildren().addAll(iconContainer, textContainer);
        return resultItem;
    }
    
    public static HBox createSearchResultItem(SearchResult result) {
        HBox resultItem = new HBox();
        resultItem.getStyleClass().add("result-item");

        StackPane iconContainer = createIconContainer(
            result.getFileType().getIconText(), 
            result.getFileType().getIconClass()
        );
        
        VBox textContainer = createTextContainer(
            result.getDisplayName(), 
            result.getPath()
        );
        
        Label shortcutLabel = createShortcutLabel();

        resultItem.getChildren().addAll(iconContainer, textContainer, shortcutLabel);
        return resultItem;
    }
    
    private static StackPane createIconContainer(String iconText, String iconClass) {
        StackPane iconContainer = new StackPane();
        iconContainer.getStyleClass().addAll("result-icon", iconClass);
        Label iconLabel = new Label(iconText);
        iconContainer.getChildren().add(iconLabel);
        return iconContainer;
    }
    
    private static VBox createTextContainer(String title, String path) {
        VBox textContainer = new VBox();
        textContainer.getStyleClass().add("result-text");
        HBox.setHgrow(textContainer, Priority.ALWAYS);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("result-title");
        textContainer.getChildren().add(titleLabel);

        if (path != null) {
            Label pathLabel = new Label(path);
            pathLabel.getStyleClass().add("result-path");
            pathLabel.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 12px;");
            textContainer.getChildren().add(pathLabel);
        }

        return textContainer;
    }
    
    private static Label createShortcutLabel() {
        Label shortcutLabel = new Label("Enter");
        shortcutLabel.getStyleClass().add("shortcut");
        return shortcutLabel;
    }
}