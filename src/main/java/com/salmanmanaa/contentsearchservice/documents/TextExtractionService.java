package com.salmanmanaa.contentsearchservice.documents;

import com.salmanmanaa.contentsearchservice.common.FileValidationException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class TextExtractionService {

    public String extractText(MultipartFile file) {
        String filename = file.getOriginalFilename();

        if (filename == null) {
            throw new FileValidationException("Uploaded file must have a filename");
        }

        String lower = filename.toLowerCase();

        try {
            if (lower.endsWith(".txt")) {
                String content = new String(file.getBytes(), StandardCharsets.UTF_8).trim();
                if (content.isBlank()) {
                    throw new FileValidationException("Uploaded file is empty");
                }
                return content;
            }

            if (lower.endsWith(".pdf")) {
                try (PDDocument document = Loader.loadPDF(file.getBytes())) {
                    String text = new PDFTextStripper().getText(document).trim();
                    if (text.isBlank()) {
                        throw new FileValidationException("PDF contains no extractable text");
                    }
                    return text;
                }
            }

            throw new FileValidationException("Only .txt and .pdf files are supported");
        } catch (IOException e) {
            throw new FileValidationException("Failed to read uploaded file");
        }
    }
}
