package com.salmanmanaa.contentsearchservice.documents;

public interface DocumentService {
    CreateDocumentResponse create(CreateDocumentRequest request);
    GetDocumentResponse getById(String id);
    IndexDocumentResponse indexById(String id);
}
