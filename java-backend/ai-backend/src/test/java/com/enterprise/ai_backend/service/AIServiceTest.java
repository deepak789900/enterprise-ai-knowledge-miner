package com.enterprise.ai_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AIServiceTest {

    private AIService aiService;

    @BeforeEach
    void setUp() {
        aiService = new AIService();
    }

    @Test
    void summarize_shouldReturnShortenedText() {
        String input = "Artificial Intelligence is transforming the world of enterprise solutions.";
        String summary = aiService.summarize(input);

        assertNotNull(summary, "Summary should not be null");
        assertTrue(summary.startsWith("Summary:"), "Summary should start with 'Summary:'");
        assertTrue(summary.length() <= input.length(), "Summary should be shorter than or equal to original text");
    }

    @Test
    void extractInsights_shouldReturnInsightText() {
        String input = "This document contains valuable business data.";
        String insight = aiService.extractInsights(input);

        assertNotNull(insight, "Insight should not be null");
        assertTrue(insight.contains("characters analyzed"), "Insight should include character analysis info");
        assertTrue(insight.length() > 10, "Insight should not be too short");
    }

    @Test
    void summarize_shouldHandleShortText() {
        String input = "Short";
        String summary = aiService.summarize(input);

        assertEquals("Summary: Short...", summary, "Summary should handle short text correctly");
    }

    @Test
    void extractInsights_shouldHandleEmptyText() {
        String input = "";
        String insight = aiService.extractInsights(input);

        assertNotNull(insight, "Insight should not be null");
        assertTrue(insight.contains("0 characters"), "Should handle empty input text gracefully");
    }
}
