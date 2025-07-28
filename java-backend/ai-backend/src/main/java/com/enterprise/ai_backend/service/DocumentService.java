package com.enterprise.ai_backend.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.enterprise.ai_backend.dto.DocumentUpdateDTO;
import com.enterprise.ai_backend.model.DocumentEntity;
import com.enterprise.ai_backend.repository.DocumentRepository;

@Service
public class DocumentService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    private final DocumentRepository documentRepository;
    private final FileParserService fileParserService;
    private final AIEngineService aiEngineService;
    private final ElasticService elasticService;

    @Autowired
    public DocumentService(
            DocumentRepository documentRepository,
            FileParserService fileParserService,
            AIEngineService aiEngineService,
            ElasticService elasticService
    ) {
        this.documentRepository = documentRepository;
        this.fileParserService = fileParserService;
        this.aiEngineService = aiEngineService;
        this.elasticService = elasticService;
    }

    public DocumentEntity processAndSave(MultipartFile file) throws IOException {
        String filename = Optional.ofNullable(file.getOriginalFilename()).orElse("unknown").trim();
        logger.info("📥 Upload received for '{}'", filename);

        // ✅ Check for existing document by fileName
        List<DocumentEntity> existingDocs = documentRepository.findByFileName(filename);
        if (!existingDocs.isEmpty()) {
            logger.warn("⚠️ Duplicate file name detected for '{}'. Skipping upload.", filename);
            return existingDocs.get(0); // Return existing document instead of saving a new one
        }

        // ✅ Parse content
        String content = fileParserService.parse(file);
        logger.info("📄 Parsed file '{}'", filename);

        // ✅ Generate summary
        String summary = aiEngineService.getSummary(content);
        logger.info("🧠 Summary generated for '{}'", filename);

        // ✅ Extract keywords
        List<Map<String, Object>> keywordList = aiEngineService.getKeywords(content, 10);
        String keywords = keywordList.stream()
                .map(k -> (String) k.get("keyword"))
                .filter(k -> k != null && !k.isBlank())
                .map(String::trim)
                .distinct()
                .collect(Collectors.joining(", "));
        logger.info("🟡 Extracted keywords: {}", keywords.isBlank() ? "None" : keywords);

        // ✅ Extract entities
        Map<String, Object> entityMap = aiEngineService.getEntities(
                content, List.of(), false, false, false, false, false
        );

        logger.debug("Raw entityMap from AI Engine: {}", entityMap);

        List<String> entityValues = new ArrayList<>();
        if (entityMap != null && !entityMap.containsKey("error")) {
            for (Object value : entityMap.values()) {
                if (value instanceof List<?> list) {
                    for (Object item : list) {
                        if (item instanceof Map<?, ?> itemMap) {
                            Object text = itemMap.get("text");
                            if (text instanceof String str && !str.isBlank()) {
                                entityValues.add(str.trim());
                            }
                        }
                    }
                }
            }
        } else {
            logger.warn("⚠️ Entity extraction returned empty or error for '{}'", filename);
        }

        String entities = entityValues.stream()
                .distinct()
                .collect(Collectors.joining(", "));
        logger.info("🔵 Extracted entities: {}", entities.isBlank() ? "None" : entities);

        // ✅ Save to PostgreSQL
        DocumentEntity entity = new DocumentEntity();
        entity.setFileName(filename);
        entity.setContent(content);
        entity.setSummary(summary);
        entity.setKeywords(keywords);
        entity.setEntities(entities);
        entity.setCreatedAt(LocalDateTime.now());

        DocumentEntity saved = documentRepository.save(entity);
        logger.info("✅ Document '{}' saved to DB with ID {}", filename, saved.getId());

        // ✅ Index in Elasticsearch
        elasticService.indexDocument(filename, content, summary);
        logger.info("🔍 Document '{}' indexed in Elasticsearch", filename);

        return saved;
    }


    public DocumentEntity saveDocument(DocumentEntity document) {
        return documentRepository.save(document);
    }

    public List<String> searchDocuments(String query) {
        List<String> rawResults = elasticService.search(query);
        return rawResults.stream()
                .filter(result -> !result.contains("Error"))
                .distinct()
                .collect(Collectors.toList());
    }

    public List<DocumentEntity> searchDocumentsByFileNameContentOrSummary(String query) {
        return documentRepository
                .findByFileNameContainingIgnoreCaseOrContentContainingIgnoreCaseOrSummaryContainingIgnoreCase(
                        query, query, query
                );
    }

    public Optional<DocumentEntity> getDocumentById(Long id) {
        return documentRepository.findById(id);
    }

    public List<DocumentEntity> getAllDocuments() {
        return documentRepository.findAll();
    }

    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }

    public DocumentEntity updateDocument(Long id, DocumentUpdateDTO updateDTO) {
        return documentRepository.findById(id).map(existing -> {
            if (updateDTO.getFileName() != null) {
                existing.setFileName(updateDTO.getFileName());
            }
            if (updateDTO.getContent() != null) {
                existing.setContent(updateDTO.getContent());
            }
            if (updateDTO.getSummary() != null) {
                existing.setSummary(updateDTO.getSummary());
            }
            return documentRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
    }

    @Transactional
    public void removeDuplicateDocuments() {
        List<DocumentEntity> allDocs = documentRepository.findAll();

        Set<String> uniqueKeys = new HashSet<>();
        List<DocumentEntity> duplicates = new ArrayList<>();

        for (DocumentEntity doc : allDocs) {
            String key = doc.getFileName() + "::" + doc.getSummary();
            if (!uniqueKeys.add(key)) {
                duplicates.add(doc);
            }
        }

        if (!duplicates.isEmpty()) {
            documentRepository.deleteAll(duplicates);
            logger.info("🧹 Removed {} duplicate documents.", duplicates.size());
        } else {
            logger.info("✅ No duplicates found.");
        }
    }

    public void deleteAllDocuments() {
        documentRepository.deleteAll();
        logger.info("🗑️ All documents deleted from PostgreSQL.");
        elasticService.deleteAllIndices();
        logger.info("🗑️ All documents deleted from Elasticsearch.");
    }

    public List<DocumentEntity> getDocumentByFileName(String fileName) {
        return documentRepository.findByFileName(fileName);
    }
}
