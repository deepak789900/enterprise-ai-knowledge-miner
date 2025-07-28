package com.enterprise.ai_backend;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.enterprise.ai_backend.dto.DocumentResponseDTO;
import com.enterprise.ai_backend.model.DocumentEntity;
import com.enterprise.ai_backend.repository.DocumentRepository;
import com.enterprise.ai_backend.service.ElasticService;

public class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private ElasticService elasticService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFetchSummaryById_Success() {
        // Given
        Long docId = 1L;
        DocumentEntity mockDoc = new DocumentEntity();
        mockDoc.setId(docId);
        mockDoc.setFileName("test.pdf");
        mockDoc.setContent("File content...");
        mockDoc.setSummary("AI-generated summary");
        mockDoc.setCreatedAt(LocalDateTime.now());

        when(documentRepository.findById(docId)).thenReturn(Optional.of(mockDoc));

        // When
        DocumentResponseDTO result = elasticService.fetchSummaryById(docId);

        // Then
        assertNotNull(result);
        assertEquals("test.pdf", result.getFileName());
        assertEquals("AI-generated summary", result.getSummary());
    }

    @Test
    public void testFetchSummaryById_NotFound() {
        // Given
        Long docId = 999L;
        when(documentRepository.findById(docId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            elasticService.fetchSummaryById(docId);
        });
    }
}
