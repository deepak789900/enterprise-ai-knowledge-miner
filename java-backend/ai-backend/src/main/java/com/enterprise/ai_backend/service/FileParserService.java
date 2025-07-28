package com.enterprise.ai_backend.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileParserService {

    /**
     * Parses a .txt or .pdf file and returns its content as a string.
     *
     * @param file the uploaded MultipartFile
     * @return the extracted text content
     * @throws IOException if an error occurs during reading/parsing
     */
    public String parse(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (filename == null || file.isEmpty()) {
            throw new IllegalArgumentException("Invalid file: name is null or file is empty.");
        }

        if (filename.endsWith(".txt")) {
            return parseTextFile(file);
        } else if (filename.endsWith(".pdf")) {
            return parsePdfFile(file);
        } else {
            throw new IllegalArgumentException("Unsupported file type. Only .txt and .pdf are allowed.");
        }
    }

    private String parseTextFile(MultipartFile file) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString().trim(); // remove trailing newline
    }

    private String parsePdfFile(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            if (document.isEncrypted()) {
                throw new IOException("Cannot parse encrypted PDF file.");
            }
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document).trim();
        }
    }
}
