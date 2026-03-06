package com.salmanmanaa.contentsearchservice.indexing;

import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElasticsearchIndexingService {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ChunkingService chunkingService;

    public ElasticsearchIndexingService(ElasticsearchTemplate elasticsearchTemplate,
                                        ChunkingService chunkingService) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.chunkingService = chunkingService;
    }

    public int indexDocument(String documentId, String title, String content) {
        List<String> chunks = chunkingService.chunk(content);

        for (int i = 0; i < chunks.size(); i++) {
            SearchChunkDocument chunkDocument = new SearchChunkDocument(
                    documentId + "-" + i,
                    documentId,
                    title,
                    i,
                    chunks.get(i)
            );
            elasticsearchTemplate.save(chunkDocument);
        }

        elasticsearchTemplate.indexOps(SearchChunkDocument.class).refresh();

        return chunks.size();
    }
}
