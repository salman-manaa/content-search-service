package com.salmanmanaa.contentsearchservice.documents;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryDocumentService implements DocumentService {

    private final Map<String, DocumentMetadata> documents = new ConcurrentHashMap<>();

    @Override
    public CreateDocumentResponse create(CreateDocumentRequest request) {
        String id = UUID.randomUUID().toString();
        Instant createdAt = Instant.now();

        DocumentMetadata metadata = new DocumentMetadata(
                id,
                request.title(),
                request.content(),
                DocumentStatus.CREATED,
                createdAt
        );

        documents.put(id, metadata);

        return new CreateDocumentResponse(
                metadata.id(),
                metadata.title(),
                metadata.status(),
                metadata.createdAt()
        );
    }
}
