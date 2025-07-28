package com.enterprise.ai_backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enterprise.ai_backend.dto.DocumentResponseDTO;
import com.enterprise.ai_backend.model.DocumentEntity;
import com.enterprise.ai_backend.repository.DocumentRepository;

@Service
public class ElasticService {

    private final DocumentRepository documentRepository;

    @Autowired
    public ElasticService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    /**
     * Indexes the document content and summary into PostgreSQL
     * (placeholder for future Elasticsearch integration).
     */
    public Long indexDocument(String filename, String content, String summary) {
        DocumentEntity doc = new DocumentEntity();
        doc.setFileName(filename);
        doc.setContent(content);
        doc.setSummary(summary);
        doc.setCreatedAt(LocalDateTime.now());

        DocumentEntity saved = documentRepository.save(doc);
        return saved.getId();
    }

    /**
     * Fetches the document summary by its ID.
     */
    public DocumentResponseDTO fetchSummaryById(Long id) {
        Optional<DocumentEntity> optionalDoc = documentRepository.findById(id);
        if (optionalDoc.isPresent()) {
            DocumentEntity doc = optionalDoc.get();
            return new DocumentResponseDTO(
                doc.getId(),
                doc.getFileName(),
                doc.getSummary(),
                doc.getCreatedAt()
            );
        } else {
            throw new RuntimeException("Document not found with ID: " + id);
        }
    }

    /**
     * Simulated full-text search using content and summary (PostgreSQL LIKE-based).
     */
    public List<String> search(String query) {
        List<DocumentEntity> allDocs = documentRepository.findAll();
        return allDocs.stream()
            .filter(doc -> (doc.getContent() != null && doc.getContent().toLowerCase().contains(query.toLowerCase()))
                        || (doc.getSummary() != null && doc.getSummary().toLowerCase().contains(query.toLowerCase())))
            .map(doc -> "📄 " + doc.getFileName() + " → " + doc.getSummary())
            .collect(Collectors.toList());
    }

    /**
     * Deletes all indexed documents (placeholder for Elasticsearch cleanup).
     */
    public void deleteAllIndices() {
        documentRepository.deleteAll();
    }
}
