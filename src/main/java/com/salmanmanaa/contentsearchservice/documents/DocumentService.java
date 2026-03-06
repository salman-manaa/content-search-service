package com.salmanmanaa.contentsearchservice.documents;

import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {
    CreateDocumentResponse create(CreateDocumentRequest request);
    CreateDocumentResponse createFromTextFile(MultipartFile file);
    GetDocumentResponse getById(String id);
    IndexDocumentResponse indexById(String id);
}
