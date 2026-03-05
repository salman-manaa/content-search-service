package com.salmanmanaa.contentsearchservice.api;

import com.salmanmanaa.contentsearchservice.documents.CreateDocumentRequest;
import com.salmanmanaa.contentsearchservice.documents.CreateDocumentResponse;
import com.salmanmanaa.contentsearchservice.documents.DocumentService;
import com.salmanmanaa.contentsearchservice.documents.GetDocumentResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateDocumentResponse create(@Valid @RequestBody CreateDocumentRequest request) {
        return documentService.create(request);
    }

    @GetMapping("/{id}")
    public GetDocumentResponse getById(@PathVariable String id) {
        return documentService.getById(id);
    }
}
