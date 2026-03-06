package com.salmanmanaa.contentsearchservice.documents;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {
    CreateDocumentResponse create(CreateDocumentRequest request);
    CreateDocumentResponse createFromTextFile(MultipartFile file);
    List<ListDocumentResponse> listAll();
    GetDocumentResponse getById(String id);
    IndexDocumentResponse indexById(String id);
}
