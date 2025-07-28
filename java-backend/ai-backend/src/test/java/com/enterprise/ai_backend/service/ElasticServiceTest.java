package com.enterprise.ai_backend.service;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.enterprise.ai_backend.dto.DocumentResponseDTO;
import com.enterprise.ai_backend.model.DocumentEntity;
import com.enterprise.ai_backend.repository.DocumentRepository;

class ElasticServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private ElasticService elasticService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIndexDocument_success() {
        // Arrange
        DocumentEntity mockDoc = new DocumentEntity();
        mockDoc.setId(1L);
        mockDoc.setFileName("test.pdf");
        mockDoc.setContent("File content...");
        mockDoc.setSummary("AI-generated summary");
        mockDoc.setCreatedAt(LocalDateTime.now());

        when(documentRepository.save(any(DocumentEntity.class))).thenReturn(mockDoc);

        // Act
        Long docId = elasticService.indexDocument(
                mockDoc.getFileName(),
                mockDoc.getContent(),
                mockDoc.getSummary()
        );

        // Assert
        assertNotNull(docId);
        assertEquals(1L, docId);
        verify(documentRepository, times(1)).save(any(DocumentEntity.class));
    }

    @Test
    void testFetchSummaryById_success() {
        // Arrange
        Long id = 1L;
        DocumentEntity mockDoc = new DocumentEntity();
        mockDoc.setId(id);
        mockDoc.setFileName("test.pdf");
        mockDoc.setSummary("AI-generated summary");
        mockDoc.setCreatedAt(LocalDateTime.now());

        when(documentRepository.findById(id)).thenReturn(Optional.of(mockDoc));

        // Act
        DocumentResponseDTO dto = elasticService.fetchSummaryById(id);

        // Assert
        assertNotNull(dto);
        assertEquals("test.pdf", dto.getFileName());
        assertEquals("AI-generated summary", dto.getSummary());
    }

    @Test
    void testFetchSummaryById_notFound() {
        // Arrange
        Long id = 999L;
        when(documentRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> elasticService.fetchSummaryById(id));
    }
}
