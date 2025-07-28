package com.enterprise.ai_backend.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.enterprise.ai_backend.model.DocumentEntity;
import com.enterprise.ai_backend.service.DocumentService;

@CrossOrigin(origins = "http://localhost:5173") // ✅ Allow frontend connection
@RestController
@RequestMapping("/api")
public class UploadController {

    @Autowired
    private DocumentService documentService;

    // ✅ Upload endpoint
    @PostMapping("/upload")
    public ResponseEntity<DocumentEntity> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            DocumentEntity saved = documentService.processAndSave(file);
            return ResponseEntity.ok(saved);
        } catch (IOException e) {
            e.printStackTrace(); // Optional: helpful for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ❌ Removed duplicate /documents endpoint to avoid conflict
}
