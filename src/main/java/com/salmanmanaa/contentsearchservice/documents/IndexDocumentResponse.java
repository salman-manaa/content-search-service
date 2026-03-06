package com.salmanmanaa.contentsearchservice.documents;

public record IndexDocumentResponse(
        String id,
        DocumentStatus status,
        int chunkCount
) {
}
