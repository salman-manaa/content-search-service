package com.salmanmanaa.contentsearchservice.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void searchReturnsResultsAfterIndexing() throws Exception {
        String uniqueTerm = "UNIQUE_SEARCH_TOKEN_314159";

        String requestBody = """
                {
                  "title": "Searchable Document",
                  "content": "%s makes content searchable."
                }
                """.formatted(uniqueTerm);

        String createResponse = mockMvc.perform(post("/api/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(createResponse);
        String id = json.get("id").asText();

        mockMvc.perform(post("/api/documents/{id}/index", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.status", is("INDEXED")))
                .andExpect(jsonPath("$.chunkCount", greaterThanOrEqualTo(1)));

        mockMvc.perform(get("/api/search").param("q", uniqueTerm))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].documentId", hasItem(id)))
                .andExpect(jsonPath("$[*].title", hasItem("Searchable Document")));
    }
}
