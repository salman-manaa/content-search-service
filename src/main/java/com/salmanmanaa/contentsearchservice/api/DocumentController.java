package com.salmanmanaa.contentsearchservice.api;

import com.salmanmanaa.contentsearchservice.documents.CreateDocumentRequest;
import com.salmanmanaa.contentsearchservice.documents.CreateDocumentResponse;
import com.salmanmanaa.contentsearchservice.documents.DocumentService;
import com.salmanmanaa.contentsearchservice.documents.GetDocumentResponse;
import com.salmanmanaa.contentsearchservice.documents.IndexDocumentResponse;
import com.salmanmanaa.contentsearchservice.documents.ListDocumentResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateDocumentResponse uploadTextFile(@RequestParam("file") MultipartFile file) {
        return documentService.createFromTextFile(file);
    }

    @GetMapping
    public List<ListDocumentResponse> listAll() {
        return documentService.listAll();
    }

    @GetMapping("/{id}")
    public GetDocumentResponse getById(@PathVariable String id) {
        return documentService.getById(id);
    }

    @PostMapping("/{id}/index")
    public IndexDocumentResponse indexById(@PathVariable String id) {
        return documentService.indexById(id);
    }
}
