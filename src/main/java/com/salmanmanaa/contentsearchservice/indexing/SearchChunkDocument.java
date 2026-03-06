package com.salmanmanaa.contentsearchservice.indexing;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "content_chunks")
public class SearchChunkDocument {

    @Id
    private String id;

    private String documentId;
    private String title;
    private Integer chunkIndex;
    private String content;

    public SearchChunkDocument() {
    }

    public SearchChunkDocument(String id, String documentId, String title, Integer chunkIndex, String content) {
        this.id = id;
        this.documentId = documentId;
        this.title = title;
        this.chunkIndex = chunkIndex;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getTitle() {
        return title;
    }

    public Integer getChunkIndex() {
        return chunkIndex;
    }

    public String getContent() {
        return content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setChunkIndex(Integer chunkIndex) {
        this.chunkIndex = chunkIndex;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
