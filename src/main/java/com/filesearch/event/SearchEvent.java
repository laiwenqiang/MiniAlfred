package com.filesearch.event;

import java.time.Instant;

/**
 * @author Lion
 * @date 2025/2/2 22:35
 */
public class SearchEvent {
    private final String query;
    private final Instant timestamp = Instant.now();

    public SearchEvent(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
