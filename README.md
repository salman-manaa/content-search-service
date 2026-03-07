# Content Search Service

A Spring Boot backend for uploading documents, extracting text, indexing content in Elasticsearch, and searching it through REST APIs.

This project demonstrates backend API design, file handling, search indexing, and automated testing in a small but complete service.

## What it shows

- REST endpoints for document creation, upload, indexing, and search
- PDF and TXT ingestion with text extraction for uploaded files
- Elasticsearch integration for chunk-based keyword search
- Test coverage for core API flows with JUnit 5 and MockMvc

## Demo

![Terminal demo](docs/demo-terminal.png)

## Stack

- Java 21
- Spring Boot
- Spring Data Elasticsearch
- Elasticsearch
- Docker Compose
- JUnit 5
- MockMvc

## Key features

- Create and retrieve documents through REST APIs
- Upload TXT and PDF files through multipart requests
- Extract text from PDFs and split content into search chunks
- Index chunks into Elasticsearch and search by keyword
- Verify core flows with API-level tests

## Run locally

1. Start Elasticsearch:

```bash
docker compose up -d
```

2. Run the application:

```bash
./mvnw spring-boot:run
```

3. Check the service:

```bash
curl http://localhost:8080/api/health/ping
```

## Example usage

Create a document:

```bash
curl -X POST http://localhost:8080/api/documents \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Index Test",
    "content": "This is a test document that will be indexed and searched."
  }'
```

Upload and index a file:

```bash
curl -X POST http://localhost:8080/api/documents/upload \
  -F "file=@sample-data/example.txt"
```

Replace `<DOCUMENT_ID>` with the ID returned by the upload or create response.

```bash
curl -X POST http://localhost:8080/api/documents/<DOCUMENT_ID>/index
```

Search indexed content:

```bash
curl "http://localhost:8080/api/search?q=indexed"
```

## Testing

```bash
./mvnw test
```

## Future improvements

- Persistent metadata storage instead of in-memory storage
- Search snippets and highlighting
- Support for additional file formats
