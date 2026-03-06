package com.salmanmanaa.contentsearchservice.indexing;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChunkingService {

    private static final int CHUNK_SIZE = 200;

    public List<String> chunk(String text) {
        List<String> chunks = new ArrayList<>();

        if (text == null || text.isBlank()) {
            return chunks;
        }

        String normalized = text.trim().replaceAll("\\s+", " ");

        for (int start = 0; start < normalized.length(); start += CHUNK_SIZE) {
            int end = Math.min(start + CHUNK_SIZE, normalized.length());
            chunks.add(normalized.substring(start, end));
        }

        return chunks;
    }
}
