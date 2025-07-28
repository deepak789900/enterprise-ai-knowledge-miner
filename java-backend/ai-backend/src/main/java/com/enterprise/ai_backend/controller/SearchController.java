package com.enterprise.ai_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enterprise.ai_backend.service.ElasticService;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "http://localhost:5173")
public class SearchController {

    @Autowired
    private ElasticService elasticService;

    // GET /api/search?query=ai
    @GetMapping
    public List<String> searchDocuments(@RequestParam("query") String query) {
        return elasticService.search(query);
    }
}
