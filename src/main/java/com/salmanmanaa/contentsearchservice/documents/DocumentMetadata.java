package com.salmanmanaa.contentsearchservice.documents;

import java.time.Instant;

public record DocumentMetadata(
        String id,
        String title,
        String content,
        DocumentStatus status,
        Instant createdAt
) {
}
