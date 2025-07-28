package com.enterprise.ai_backend.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.enterprise.ai_backend.model.DocumentEntity;

@DataJpaTest
class DocumentRepositoryTest {

    @Autowired
    private DocumentRepository documentRepository;

    @Test
    @DisplayName("Should save and fetch a DocumentEntity successfully")
    void testSaveAndFindById() {
        // Arrange
        DocumentEntity document = new DocumentEntity();
        document.setFileName("sample.pdf");                             // ✅ correct method
        document.setContent("This is a test document.");
        document.setSummary("Test summary");
        document.setCreatedAt(LocalDateTime.now());                     // ✅ optional but good for full test

        // Act
        DocumentEntity saved = documentRepository.save(document);
        Optional<DocumentEntity> retrieved = documentRepository.findById(saved.getId());

        // Assert
        assertTrue(retrieved.isPresent());
        assertEquals("sample.pdf", retrieved.get().getFileName());      // ✅ correct method
        assertEquals("This is a test document.", retrieved.get().getContent());
        assertEquals("Test summary", retrieved.get().getSummary());
    }
}
