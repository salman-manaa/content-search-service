package com.salmanmanaa.contentsearchservice.search;

import java.util.List;

public interface SearchService {
    List<SearchResultResponse> search(String query);
}
