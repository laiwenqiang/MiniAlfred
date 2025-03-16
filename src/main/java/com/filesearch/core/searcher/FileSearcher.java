package com.filesearch.core.searcher;

/**
 * @author Lion
 * @date 2025/2/2 22:31
 */

import com.filesearch.core.model.SearchResult;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class FileSearcher {
    private static final int MAX_RESULTS = 15;
    private final Path searchPath;

    public FileSearcher(Path searchPath) {
        this.searchPath = searchPath;
    }

    public List<SearchResult> search(String query) throws IOException {
        if (StringUtils.isEmpty(query)) {
            return Collections.emptyList();
        }

        try (Stream<Path> paths = Files.walk(searchPath)) {
            return paths.filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().toLowerCase().contains(query.toLowerCase()))
                    .limit(MAX_RESULTS)
                    .map(SearchResult::new)
                    .collect(Collectors.toList());
        }
    }
}
