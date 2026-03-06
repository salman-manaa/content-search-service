package com.salmanmanaa.contentsearchservice.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createDocumentReturns201() throws Exception {
        String requestBody = """
                {
                  "title": "Test Document",
                  "content": "This is a sample document."
                }
                """;

        mockMvc.perform(post("/api/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Test Document")))
                .andExpect(jsonPath("$.status", is("CREATED")));
    }

    @Test
    void uploadTxtDocumentReturns201() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "example.txt",
                "text/plain",
                "Uploaded text content".getBytes()
        );

        mockMvc.perform(multipart("/api/documents/upload").file(file))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("example")))
                .andExpect(jsonPath("$.status", is("CREATED")));
    }

    @Test
    void uploadInvalidFileTypeReturns400() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "bad.json",
                "application/json",
                "{\"hello\":\"world\"}".getBytes()
        );

        mockMvc.perform(multipart("/api/documents/upload").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Only .txt files are supported")));
    }

    @Test
    void getDocumentByIdReturns200() throws Exception {
        String requestBody = """
                {
                  "title": "Stored Document",
                  "content": "Stored content"
                }
                """;

        String responseBody = mockMvc.perform(post("/api/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(responseBody);
        String id = json.get("id").asText();

        mockMvc.perform(get("/api/documents/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.title", is("Stored Document")))
                .andExpect(jsonPath("$.content", is("Stored content")))
                .andExpect(jsonPath("$.status", is("CREATED")));
    }

    @Test
    void getMissingDocumentReturns404() throws Exception {
        mockMvc.perform(get("/api/documents/{id}", "does-not-exist"))
                .andExpect(status().isNotFound());
    }
}
