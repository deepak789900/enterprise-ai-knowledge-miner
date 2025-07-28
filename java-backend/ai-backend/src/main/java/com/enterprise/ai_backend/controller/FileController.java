package com.enterprise.ai_backend.controller;

import com.enterprise.ai_backend.dto.DocumentResponseDTO;
import com.enterprise.ai_backend.service.AIService;
import com.enterprise.ai_backend.service.ElasticService;
import com.enterprise.ai_backend.service.FileParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileParserService fileParserService;
    private final AIService aiService;
    private final ElasticService elasticService;

    @Autowired
    public FileController(FileParserService fileParserService,
                          AIService aiService,
                          ElasticService elasticService) {
        this.fileParserService = fileParserService;
        this.aiService = aiService;
        this.elasticService = elasticService;
    }

    @PostMapping("/upload")
    public ResponseEntity<DocumentResponseDTO> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Step 1: Extract content
            String content = fileParserService.parse(file);

            // Step 2: Generate summary
            String summary = aiService.generateSummary(content);

            // Step 3: Save document and get ID
            Long documentId = elasticService.indexDocument(
                    file.getOriginalFilename(),
                    content,
                    summary
            );

            // Step 4: Fetch saved doc and return response DTO
            DocumentResponseDTO response = elasticService.fetchSummaryById(documentId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/summary/{id}")
    public ResponseEntity<DocumentResponseDTO> getSummaryById(@PathVariable Long id) {
        try {
            DocumentResponseDTO dto = elasticService.fetchSummaryById(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
