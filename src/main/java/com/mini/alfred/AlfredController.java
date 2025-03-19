package com.mini.alfred;

import com.mini.alfred.ui.SearchResult;
import com.mini.alfred.ui.SearchResultManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class AlfredController implements Initializable {
    @FXML
    private TextField searchInput;
    @FXML
    private ScrollPane resultsScroll;
    @FXML
    private VBox resultsContainer;

    private FileSearcher fileSearcher;
    private SearchResultManager resultManager;
    private CompletableFuture<Void> searchTask;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileSearcher = new FileSearcher();
        resultManager = new SearchResultManager(resultsContainer, resultsScroll);

        initializeSearchInput();
        initializeKeyboardNavigation();
    }

    private void initializeSearchInput() {
        resultsContainer.setFocusTraversable(true);
        searchInput.textProperty().addListener((observable, oldValue, newValue) -> 
            performSearch(newValue));
    }

    private void initializeKeyboardNavigation() {
        searchInput.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case DOWN:
                    resultManager.selectNext();
                    scrollToSelectedItem();
                    break;
                case UP:
                    resultManager.selectPrevious();
                    scrollToSelectedItem();
                    break;
                case ENTER:
                    handleSelection();
                    break;
                default:
                    return;
            }
            event.consume();
        });
    }

    private void performSearch(String query) {
        if (searchTask != null && !searchTask.isDone()) {
            searchTask.cancel(true);
        }

        if (StringUtils.isBlank(query)) {
            resultManager.clearResults();
            return;
        }

        searchTask = fileSearcher.searchAsync(query)
            .thenAccept(files -> Platform.runLater(() -> {
                List<SearchResult> results = files.stream()
                    .map(SearchResult::new)
                    .collect(Collectors.toList());
                resultManager.updateResults(results);
                if (!results.isEmpty()) {
                    resultsScroll.setVvalue(0);
                    scrollToSelectedItem();
                }
            }));
    }

    private void scrollToSelectedItem() {
        var selectedItem = resultManager.getSelectedItem();
        if (selectedItem == null || resultsScroll == null) {
            return;
        }

        // 使用异步确保布局计算完成
        Platform.runLater(() -> {
            // 计算选中项在容器中的相对位置
            double itemMinY = selectedItem.getBoundsInParent().getMinY();
            double itemMaxY = selectedItem.getBoundsInParent().getMaxY();
            double containerHeight = resultsContainer.getHeight();
            double viewportHeight = resultsScroll.getViewportBounds().getHeight();

            // 动态调整滚动位置
            if (itemMaxY > viewportHeight) {
                double targetVvalue = itemMinY / (containerHeight - viewportHeight);
                resultsScroll.setVvalue(targetVvalue);
            } else if (itemMinY < resultsScroll.getVvalue() * (containerHeight - viewportHeight)) {
                double targetVvalue = itemMinY / (containerHeight - viewportHeight);
                resultsScroll.setVvalue(targetVvalue);
            }
        });
    }

    private void handleSelection() {
        SearchResult selectedResult = resultManager.getSelectedResult();
        if (selectedResult != null) {
            tryOpenFile(selectedResult.getFile());
        }
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