package com.salmanmanaa.contentsearchservice.documents;

import com.salmanmanaa.contentsearchservice.common.FileValidationException;
import com.salmanmanaa.contentsearchservice.indexing.ElasticsearchIndexingService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
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
        return storeDocument(request.title(), request.content());
    }

    @Override
    public CreateDocumentResponse createFromTextFile(MultipartFile file) {
        validateTextFile(file);

        try {
            String filename = file.getOriginalFilename() == null ? "uploaded-document.txt" : file.getOriginalFilename();
            String title = stripTxtExtension(filename);
            String content = new String(file.getBytes(), StandardCharsets.UTF_8).trim();

            if (content.isBlank()) {
                throw new FileValidationException("Uploaded file is empty");
            }

            return storeDocument(title, content);
        } catch (IOException e) {
            throw new ResponseStatusException(BAD_REQUEST, "Failed to read uploaded file");
        }
    }

    @Override
    public List<ListDocumentResponse> listAll() {
        return documents.values().stream()
                .sorted(Comparator.comparing(DocumentMetadata::createdAt).reversed())
                .map(document -> new ListDocumentResponse(
                        document.id(),
                        document.title(),
                        document.status(),
                        document.createdAt()
                ))
                .toList();
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

    private CreateDocumentResponse storeDocument(String title, String content) {
        String id = UUID.randomUUID().toString();
        Instant createdAt = Instant.now();

        DocumentMetadata metadata = new DocumentMetadata(
                id,
                title,
                content,
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

    private void validateTextFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileValidationException("Uploaded file is empty");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".txt")) {
            throw new FileValidationException("Only .txt files are supported");
        }
    }

    private String stripTxtExtension(String filename) {
        if (filename.toLowerCase().endsWith(".txt")) {
            return filename.substring(0, filename.length() - 4);
        }
        return filename;
    }
}
