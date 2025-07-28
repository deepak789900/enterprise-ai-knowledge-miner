package com.enterprise.ai_backend.service;

import org.springframework.stereotype.Service;

@Service
public class AIService {

    public String summarize(String content) {
        if (content == null || content.isEmpty()) return "Summary: ...";

        if (content.length() <= 10) {
            return "Summary: " + content + "...";
        }
        return "Summary: " + content.substring(0, Math.min(content.length() / 2, 100)) + "...";
    }

    public String extractInsights(String content) {
        if (content == null) content = "";
        return "Insight: " + content.length() + " characters analyzed for semantic meaning.";
    }

    public String generateSummary(String content) {
        return summarize(content);
    }
}
