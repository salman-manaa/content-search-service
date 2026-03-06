package com.salmanmanaa.contentsearchservice.documents;

import com.salmanmanaa.contentsearchservice.common.FileValidationException;
import com.salmanmanaa.contentsearchservice.indexing.ElasticsearchIndexingService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class InMemoryDocumentService implements DocumentService {

    private final Map<String, DocumentMetadata> documents = new ConcurrentHashMap<>();
    private final ElasticsearchIndexingService indexingService;
    private final TextExtractionService textExtractionService;

    public InMemoryDocumentService(ElasticsearchIndexingService indexingService,
                                   TextExtractionService textExtractionService) {
        this.indexingService = indexingService;
        this.textExtractionService = textExtractionService;
    }

    @Override
    public CreateDocumentResponse create(CreateDocumentRequest request) {
        return storeDocument(request.title(), request.content());
    }

    @Override
    public CreateDocumentResponse createFromTextFile(MultipartFile file) {
        validateSupportedFile(file);

        String filename = file.getOriginalFilename() == null ? "uploaded-document" : file.getOriginalFilename();
        String title = stripSupportedExtension(filename);
        String content = textExtractionService.extractText(file);

        return storeDocument(title, content);
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

    private void validateSupportedFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileValidationException("Uploaded file is empty");
        }

        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new FileValidationException("Uploaded file must have a filename");
        }

        String lower = filename.toLowerCase();
        if (!lower.endsWith(".txt") && !lower.endsWith(".pdf")) {
            throw new FileValidationException("Only .txt and .pdf files are supported");
        }
    }

    private String stripSupportedExtension(String filename) {
        String lower = filename.toLowerCase();

        if (lower.endsWith(".txt") || lower.endsWith(".pdf")) {
            return filename.substring(0, filename.lastIndexOf('.'));
        }

        return filename;
    }
}
