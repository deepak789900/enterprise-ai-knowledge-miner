package com.enterprise.ai_backend.service;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class FileParserServiceTest {

    private FileParserService fileParserService;

    @BeforeEach
    void setUp() {
        fileParserService = new FileParserService();
    }

    @Test
    void testParseTextFile_success() throws IOException {
        // Arrange: create a mock .txt file
        String textContent = "This is a test file.\nSecond line.";
        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "test.txt", "text/plain", textContent.getBytes()
        );

        // Act
        String result = fileParserService.parse(mockFile);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("This is a test file"));
    }

    @Test
    void testParsePdfFile_unsupportedFile() {
        // Arrange: create a mock .docx file
        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "test.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "Fake content".getBytes()
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
        fileParserService.parse(mockFile);
        });
    }

    @Test
    void testParseFile_nullFilename() {
        // Arrange: mock file with null filename
        MockMultipartFile mockFile = new MockMultipartFile(
                "file", null, "text/plain", "test".getBytes()
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> fileParserService.parse(mockFile));
    }
}
