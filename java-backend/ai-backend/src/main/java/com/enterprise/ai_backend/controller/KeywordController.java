package com.enterprise.ai_backend.controller;

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
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Enable CORS if not globally enabled
public class KeywordController {

    @Autowired
    private AIEngineService aiEngineService;

    @PostMapping("/keywords")
    public List<String> getKeywords(@RequestBody Map<String, String> request) {
        String content = request.get("content");
        return aiEngineService.extractKeywords(content);
    }
}
