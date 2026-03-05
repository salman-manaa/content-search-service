package com.salmanmanaa.contentsearchservice.documents;

import java.time.Instant;

public record GetDocumentResponse(
        String id,
        String title,
        String content,
        DocumentStatus status,
        Instant createdAt
) {
}
