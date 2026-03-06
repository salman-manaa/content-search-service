package com.salmanmanaa.contentsearchservice.search;

public record SearchResultResponse(
        String documentId,
        String title,
        Integer chunkIndex,
        String content,
        Float score
) {
}
