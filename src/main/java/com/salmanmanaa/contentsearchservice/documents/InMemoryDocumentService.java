package com.salmanmanaa.contentsearchservice.documents;

import com.salmanmanaa.contentsearchservice.indexing.ElasticsearchIndexingService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class InMemoryDocumentService implements DocumentService {

    private final Map<String, DocumentMetadata> documents = new ConcurrentHashMap<>();
    private final ElasticsearchIndexingService indexingService;

    public InMemoryDocumentService(ElasticsearchIndexingService indexingService) {
        this.indexingService = indexingService;
    }

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

    @Override
    public GetDocumentResponse getById(String id) {
        DocumentMetadata metadata = documents.get(id);

        if (metadata == null) {
            throw new ResponseStatusException(NOT_FOUND, "Document not found");
        }

        return new GetDocumentResponse(
                metadata.id(),
                metadata.title(),
                metadata.content(),
                metadata.status(),
                metadata.createdAt()
        );
    }

    @Override
    public IndexDocumentResponse indexById(String id) {
        DocumentMetadata metadata = documents.get(id);

        if (metadata == null) {
            throw new ResponseStatusException(NOT_FOUND, "Document not found");
        }

        int chunkCount = indexingService.indexDocument(
                metadata.id(),
                metadata.title(),
                metadata.content()
        );

        DocumentMetadata updated = new DocumentMetadata(
                metadata.id(),
                metadata.title(),
                metadata.content(),
                DocumentStatus.INDEXED,
                metadata.createdAt()
        );

        documents.put(id, updated);

        return new IndexDocumentResponse(
                updated.id(),
                updated.status(),
                chunkCount
        );
    }
}
