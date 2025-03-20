package com.mini.alfred.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class SearchResultItemFactory {
    
    public static HBox createSearchResultItem(SearchResult result) {
        StackPane iconContainer = getIconContainer(result);
        VBox textContainer = getTextContainer(result);
        Label shortcutLabel = getShortcutLabel();

        HBox resultItem = new HBox();
        resultItem.getStyleClass().add("result-item");
        resultItem.getChildren().addAll(iconContainer, textContainer, shortcutLabel);

        return resultItem;
    }

    private static VBox getTextContainer(SearchResult result) {
        VBox textContainer = new VBox();
        textContainer.getStyleClass().add("result-text");
        HBox.setHgrow(textContainer, Priority.ALWAYS);

        Label titleLabel = new Label(result.getTitle());
        titleLabel.getStyleClass().add("result-title");

        Label subtitleLabel = new Label(result.getSubtitle());
        subtitleLabel.getStyleClass().add("result-path");

        textContainer.getChildren().addAll(titleLabel, subtitleLabel);

        if (result.getType() == SearchResult.ResultType.CALCULATOR) {
            // 只显示图标和结果
            Label resultLabel = new Label(result.getTitle());
            resultLabel.getStyleClass().add("calculator-result");

            // 不添加副标题
            textContainer.getChildren().clear();
            textContainer.getChildren().add(resultLabel);
        }
        return textContainer;
    }

    private static Label getShortcutLabel() {
        Label shortcutLabel = new Label("Enter");
        shortcutLabel.getStyleClass().add("shortcut");
        return shortcutLabel;
    }

    private static StackPane getIconContainer(SearchResult result) {
        StackPane iconContainer = new StackPane();
        iconContainer.getStyleClass().addAll("result-icon", result.getIconClass());
        Label iconLabel = new Label(result.getIconText());
        iconContainer.getChildren().add(iconLabel);
        return iconContainer;
    }
}