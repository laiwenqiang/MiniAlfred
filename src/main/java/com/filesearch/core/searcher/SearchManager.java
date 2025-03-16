package com.filesearch.core.searcher;

import com.filesearch.core.model.SearchResult;
import com.filesearch.event.SearchEvent;
import com.filesearch.event.SearchResultsEvent;
import com.filesearch.setting.ConfigManager;
import com.filesearch.event.EventBus;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Lion
 * @date 2025/2/2 22:32
 */
public class SearchManager {
    private static SearchManager instance;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private Future<?> currentSearch;

    private SearchManager() {
        EventBus.getInstance().subscribe(SearchEvent.class, this::handleSearch);
    }

    public static synchronized SearchManager getInstance() {
        if (instance == null) {
            instance = new SearchManager();
        }
        return instance;
    }

    public void handleSearch(SearchEvent event) {
        if (currentSearch != null && !currentSearch.isDone()) {
            currentSearch.cancel(true);
        }

        currentSearch = executor.submit(() -> performSearch(event.getQuery()));
    }

    private void performSearch(String query) {
        try {
            List<SearchResult> results = new FileSearcher(ConfigManager.getInstance().getSearchPath()).search(query);
            SwingUtilities.invokeLater(() -> {
                results.forEach(r -> r.highlightMatches(query));
                EventBus.getInstance().publish(new SearchResultsEvent(results));
            });
        } catch (Exception ex) {
            ex.getMessage();
        }
    }
}
