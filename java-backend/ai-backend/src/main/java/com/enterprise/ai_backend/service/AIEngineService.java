package com.enterprise.ai_backend.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AIEngineService {

    private static final Logger logger = LoggerFactory.getLogger(AIEngineService.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "http://localhost:8000";

    public String getSummary(String content) {
        if (content == null || content.isBlank()) {
            logger.warn("❌ Empty or null input received for summarization.");
            return "Error: Content is empty or null.";
        }

        String url = BASE_URL + "/summarize/";
        Map<String, String> request = new HashMap<>();
        request.put("text", content);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        try {
            logger.info("📡 Calling AI Engine /summarize with content length: {}", content.length());
            ResponseEntity<Map<String, Object>> response = restTemplate.postForEntity(
                url, entity, (Class<Map<String, Object>>) (Class<?>) Map.class);
            Map<String, Object> body = response.getBody();

            if (body != null && body.containsKey("summary")) {
                return body.get("summary").toString().trim();
            } else {
                logger.warn("⚠️ No 'summary' found in response body.");
                return "Error: No summary found in response.";
            }
        } catch (Exception e) {
            logger.error("🔥 Exception during /summarize call", e);
            return "Error while calling AI Engine /summarize: " + e.getMessage();
        }
    }

    public List<Map<String, Object>> getKeywords(String content, int topK) {
        if (content == null || content.isBlank()) {
            logger.warn("❌ Empty or null input received for keyword extraction.");
            return List.of(Map.of("error", "Content is empty or null"));
        }

        String url = BASE_URL + "/keywords/";
        Map<String, Object> request = new HashMap<>();
        request.put("text", content);
        request.put("top_k", topK);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
            logger.info("📡 Calling AI Engine /keywords with top_k: {}", topK);
            ResponseEntity<Map<String, Object>> response = restTemplate.postForEntity(
                url, entity, (Class<Map<String, Object>>) (Class<?>) Map.class);
            Map<String, Object> body = response.getBody();

            if (body != null && body.containsKey("keywords")) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> keywords = (List<Map<String, Object>>) body.get("keywords");
                return keywords;
            } else {
                logger.warn("⚠️ No 'keywords' found in response.");
                return List.of(Map.of("error", "No keywords found in response"));
            }
        } catch (Exception e) {
            logger.error("🔥 Exception during /keywords call", e);
            return List.of(Map.of("error", "Exception while calling /keywords: " + e.getMessage()));
        }
    }

    public Map<String, Object> getEntities(String content, List<String> filterLabels,
                                           boolean uniqueOnly, boolean groupByLabel,
                                           boolean returnFreq, boolean sortByFreq,
                                           boolean returnSpans) {
        if (content == null || content.isBlank()) {
            logger.warn("❌ Empty or null input received for entity extraction.");
            return Map.of("error", "Content is empty or null");
        }

        String url = BASE_URL + "/entities/";
        Map<String, Object> request = new HashMap<>();
        request.put("text", content);
        request.put("filter_labels", filterLabels);
        request.put("unique_only", uniqueOnly);
        request.put("group_by_label", groupByLabel);
        request.put("return_freq", returnFreq);
        request.put("sort_by_freq", sortByFreq);
        request.put("return_spans", returnSpans);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
            logger.info("📡 Calling AI Engine /entities with flags: uniqueOnly={}, groupByLabel={}, returnFreq={}, sortByFreq={}, returnSpans={}",
                uniqueOnly, groupByLabel, returnFreq, sortByFreq, returnSpans);

            ResponseEntity<Map<String, Object>> response = restTemplate.postForEntity(
                url, entity, (Class<Map<String, Object>>) (Class<?>) Map.class);

            Map<String, Object> body = response.getBody();
            if (body != null) {
                return body;
            } else {
                logger.warn("⚠️ Empty response from /entities.");
                return Map.of("error", "Empty response from /entities");
            }
        } catch (Exception e) {
            logger.error("🔥 Exception during /entities call", e);
            return Map.of("error", "Exception while calling /entities: " + e.getMessage());
        }
    }

    public List<String> extractKeywords(String content) {
    if (content == null || content.isBlank()) {
        logger.warn("❌ Empty or null input received for extractKeywords()");
        return List.of();
    }

    String url = BASE_URL + "/keywords/";
    Map<String, Object> request = new HashMap<>();
    request.put("text", content);
    request.put("top_k", 10);  // You can make this dynamic if needed

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

    try {
        logger.info("📡 Calling AI Engine /keywords (extractKeywords)");
        ResponseEntity<Map<String, Object>> response = restTemplate.postForEntity(
            url, entity, (Class<Map<String, Object>>) (Class<?>) Map.class);

        Map<String, Object> body = response.getBody();

        if (body != null && body.containsKey("keywords")) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> keywords = (List<Map<String, Object>>) body.get("keywords");

            return keywords.stream()
                    .map(m -> (String) m.get("keyword"))
                    .filter(k -> k != null && !k.isBlank())
                    .map(String::trim)
                    .collect(Collectors.toList());
        } else {
            logger.warn("⚠️ No 'keywords' found in extractKeywords response.");
            return List.of();
        }
    } catch (Exception e) {
        logger.error("🔥 Exception during extractKeywords", e);
        return List.of();
    }
}
}
