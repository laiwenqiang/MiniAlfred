package com.filesearch.event;

import com.filesearch.core.model.SearchResult;

import java.util.List;

/**
 * @author Lion
 * @date 2025/2/3 16:18
 */
public record SearchResultsEvent(List<SearchResult> results) {}
