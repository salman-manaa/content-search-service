package com.salmanmanaa.contentsearchservice.documents;

import jakarta.validation.constraints.NotBlank;

public record CreateDocumentRequest(
        @NotBlank String title,
        @NotBlank String content
) {
}
