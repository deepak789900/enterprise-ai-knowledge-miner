package com.enterprise.ai_backend.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.enterprise.ai_backend.dto.DocumentUpdateDTO;
import com.enterprise.ai_backend.model.DocumentEntity;
import com.enterprise.ai_backend.service.DocumentService;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    // ✅ Upload a document
    @PostMapping("/upload")
    public ResponseEntity<DocumentEntity> uploadDocument(@RequestParam("file") MultipartFile file) throws IOException {
        DocumentEntity saved = documentService.processAndSave(file);
        return ResponseEntity.ok(saved);
    }

    // ✅ Fetch all documents
    @GetMapping
    public ResponseEntity<List<DocumentEntity>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    // ✅ Search documents by fileName, content or summary
    @GetMapping("/search")
    public ResponseEntity<List<DocumentEntity>> searchDocuments(@RequestParam("q") String query) {
        List<DocumentEntity> results = documentService.searchDocumentsByFileNameContentOrSummary(query);
        return ResponseEntity.ok(results);
    }

    // ✅ Get a document by ID
    @GetMapping("/{id}")
    public ResponseEntity<DocumentEntity> getDocumentById(@PathVariable Long id) {
        return documentService.getDocumentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Get documents by filename (not necessarily unique)
    @GetMapping("/by-filename")
    public ResponseEntity<List<DocumentEntity>> getDocumentsByFileName(@RequestParam("name") String fileName) {
        List<DocumentEntity> docs = documentService.getDocumentByFileName(fileName);
        if (docs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(docs);
    }

    // ✅ Update a document by ID
    @PutMapping("/{id}")
    public ResponseEntity<DocumentEntity> updateDocument(
            @PathVariable Long id,
            @RequestBody DocumentUpdateDTO updateDTO) {
        DocumentEntity updated = documentService.updateDocument(id, updateDTO);
        return ResponseEntity.ok(updated);
    }

    // ✅ Delete a document by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Delete all documents
    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllDocuments() {
        documentService.deleteAllDocuments();
        return ResponseEntity.noContent().build();
    }
}
