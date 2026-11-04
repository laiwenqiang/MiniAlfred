package com.mini.alfred.ui;

import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class SearchResultManager {
    private final VBox resultsContainer;
    private final ScrollPane resultsScroll;
    private final List<HBox> resultItems;
    private final List<SearchResult> searchResults;
    private final Consumer<SearchResult> onResultClicked;
    private int selectedIndex;

    public SearchResultManager(VBox resultsContainer, ScrollPane resultsScroll, Consumer<SearchResult> onResultClicked) {
        this.resultsContainer = resultsContainer;
        this.resultsScroll = resultsScroll;
        this.onResultClicked = onResultClicked;
        this.resultItems = new ArrayList<>();
        this.searchResults = new ArrayList<>();
        this.selectedIndex = 0;
        
        setResultsVisibility(false);
    }

    public void clearResults() {
        clearAllResults();
        setResultsVisibility(false);
    }

    public void updateResults(List<SearchResult> results) {
        clearAllResults();

        boolean hasResults = !results.isEmpty();
        setResultsVisibility(hasResults);

        if (hasResults) {
            searchResults.addAll(results);
            results.forEach(this::addSearchResultItem);
            selectedIndex = 0;
            updateSelection();
        }
    }

    private void setResultsVisibility(boolean visible) {
        resultsContainer.setVisible(visible);
        resultsScroll.setVisible(visible);
        resultsScroll.setManaged(visible);
    }

    private void clearAllResults() {
        resultsContainer.getChildren().clear();
        resultItems.clear();
        searchResults.clear();
    }

    private void addSearchResultItem(SearchResult result) {
        HBox item = SearchResultItemFactory.createSearchResultItem(result);
        
        int index = resultItems.size();
        item.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                selectItem(index);
                onResultClicked.accept(result);
            }
        });
        item.setOnMouseEntered(event -> selectItem(index));
        
        resultsContainer.getChildren().add(item);
        resultItems.add(item);
    }

    public void selectNext() {
        if (!resultItems.isEmpty()) {
            selectedIndex = (selectedIndex + 1) % resultItems.size();
            updateSelection();
        }
    }

    public void selectPrevious() {
        if (!resultItems.isEmpty()) {
            selectedIndex = (selectedIndex - 1 + resultItems.size()) % resultItems.size();
            updateSelection();
        }
    }

    public void selectItem(int index) {
        if (index >= 0 && index < resultItems.size()) {
            selectedIndex = index;
            updateSelection();
        }
    }

    /**
     * 更新所有结果项的选中状态
     * 为当前选中项添加selected样式类，移除其他项的selected样式类
     */
    private void updateSelection() {
        for (int i = 0; i < resultItems.size(); i++) {
            HBox item = resultItems.get(i);
            boolean isSelected = (i == selectedIndex);
            
            if (isSelected) {
                if (!item.getStyleClass().contains("selected")) {
                    item.getStyleClass().add("selected");
                }
            } else {
                item.getStyleClass().remove("selected");
            }
        }
    }

    public SearchResult getSelectedResult() {
        if (!searchResults.isEmpty() && selectedIndex >= 0 && selectedIndex < searchResults.size()) {
            return searchResults.get(selectedIndex);
        }
        return null;
    }

    public HBox getSelectedItem() {
        if (selectedIndex >= 0 && selectedIndex < resultItems.size()) {
            return resultItems.get(selectedIndex);
        }
        return null;
    }
}
