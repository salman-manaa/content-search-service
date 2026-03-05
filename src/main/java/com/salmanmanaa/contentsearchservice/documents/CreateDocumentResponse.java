package com.salmanmanaa.contentsearchservice.documents;

import java.time.Instant;

public record CreateDocumentResponse(
        String id,
        String title,
        DocumentStatus status,
        Instant createdAt
) {
}
