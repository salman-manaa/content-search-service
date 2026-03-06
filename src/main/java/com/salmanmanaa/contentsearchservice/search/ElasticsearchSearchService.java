package com.salmanmanaa.contentsearchservice.search;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.salmanmanaa.contentsearchservice.indexing.SearchChunkDocument;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElasticsearchSearchService implements SearchService {

    private final ElasticsearchTemplate elasticsearchTemplate;

    public ElasticsearchSearchService(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public List<SearchResultResponse> search(String query) {
        Query multiMatchQuery = Query.of(q -> q.multiMatch(m -> m
                .query(query)
                .fields("title", "content")
        ));

        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(multiMatchQuery)
                .withPageable(PageRequest.of(0, 10))
                .build();

        return elasticsearchTemplate.search(searchQuery, SearchChunkDocument.class)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private SearchResultResponse toResponse(SearchHit<SearchChunkDocument> hit) {
        SearchChunkDocument content = hit.getContent();

        return new SearchResultResponse(
                content.getDocumentId(),
                content.getTitle(),
                content.getChunkIndex(),
                content.getContent(),
                hit.getScore()
        );
    }
}
