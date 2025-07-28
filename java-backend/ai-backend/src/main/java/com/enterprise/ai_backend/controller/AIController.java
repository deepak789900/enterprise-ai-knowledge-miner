package com.enterprise.ai_backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enterprise.ai_backend.service.AIEngineService;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")  // Allow CORS for frontend or other services
public class AIController {

    @Autowired
    private AIEngineService aiEngineService;

    @PostMapping("/summarize")
    public Map<String, String> summarize(@RequestBody Map<String, String> request) {
        String text = request.get("text");
        String summary = aiEngineService.getSummary(text);
        return Map.of("summary", summary);
    }

    @PostMapping("/keywords")
    public Map<String, Object> extractKeywords(@RequestBody Map<String, Object> request) {
        String text = (String) request.get("text");
        int topK = request.get("top_k") != null ? (int) request.get("top_k") : 10;
        List<Map<String, Object>> keywords = aiEngineService.getKeywords(text, topK);
        return Map.of("keywords", keywords);
    }

    @PostMapping("/entities")
    public Map<String, Object> extractEntities(@RequestBody Map<String, Object> request) {
        String text = (String) request.get("text");
        List<String> filterLabels = (List<String>) request.getOrDefault("filter_labels", new ArrayList<>());
        boolean uniqueOnly = (boolean) request.getOrDefault("unique_only", false);
        boolean groupByLabel = (boolean) request.getOrDefault("group_by_label", false);
        boolean returnFreq = (boolean) request.getOrDefault("return_freq", false);
        boolean sortByFreq = (boolean) request.getOrDefault("sort_by_freq", false);
        boolean returnSpans = (boolean) request.getOrDefault("return_spans", false);

        return aiEngineService.getEntities(
                text, filterLabels, uniqueOnly, groupByLabel, returnFreq, sortByFreq, returnSpans
        );
    }
}
