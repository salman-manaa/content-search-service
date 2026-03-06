# Content Search Service

A Spring Boot backend service for document ingestion, chunking, indexing, and keyword search over Elasticsearch.

## Why I built this

I built this project to explore backend service design for content indexing and search, with a focus on clean REST APIs, local developer setup, and production-style search workflows.

## Stack

- Java 21
- Spring Boot
- Spring Data Elasticsearch
- Elasticsearch
- Docker Compose
- JUnit 5
- MockMvc

## Features

- Create documents through a REST API
- Retrieve documents by ID
- Chunk and index document content into Elasticsearch
- Run keyword search over indexed chunks
- Basic health endpoint
- Automated API tests for core flows

## API Endpoints

### Health
- `GET /api/health/ping`

### Documents
- `POST /api/documents`
- `GET /api/documents/{id}`
- `POST /api/documents/{id}/index`

### Search
- `GET /api/search?q=...`

## Run locally

### 1. Start Elasticsearch
```bash
docker compose up -d
```

### 2. Run the application
```bash
./mvnw spring-boot:run
```

### 3. Check health
```bash
curl http://localhost:8080/api/health/ping
```

## Example usage

### Create a document
```bash
curl -X POST http://localhost:8080/api/documents \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Index Test",
    "content": "This is a test document that will be indexed and searched."
  }'
```

### Get a document by ID
```bash
curl http://localhost:8080/api/documents/<DOCUMENT_ID>
```

### Index a document
```bash
curl -X POST http://localhost:8080/api/documents/<DOCUMENT_ID>/index
```

### Search indexed content
```bash
curl "http://localhost:8080/api/search?q=indexed"
```

## Architecture

```text
Client
  -> Spring Boot REST API
      -> In-memory document store
      -> Chunking service
      -> Elasticsearch indexing
      -> Search endpoint
```

## Testing

Run tests with:

```bash
./mvnw test
```

## Future improvements

- File upload support for TXT and PDF documents
- Text extraction pipeline
- Semantic search
- Persistent metadata storage
- Search snippets and highlighting
- Better observability and metrics
