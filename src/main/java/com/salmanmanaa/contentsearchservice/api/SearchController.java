package com.salmanmanaa.contentsearchservice.api;

import com.salmanmanaa.contentsearchservice.search.SearchResultResponse;
import com.salmanmanaa.contentsearchservice.search.SearchService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@Validated
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public List<SearchResultResponse> search(@RequestParam @NotBlank String q) {
        return searchService.search(q);
    }
}
