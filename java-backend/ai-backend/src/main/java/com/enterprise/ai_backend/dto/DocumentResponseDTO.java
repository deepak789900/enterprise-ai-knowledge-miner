package com.enterprise.ai_backend.dto;

import java.time.LocalDateTime;

public class DocumentResponseDTO {

    private Long id;
    private String fileName;
    private String content;
    private String summary;
    private LocalDateTime createdAt;

    // ✅ Default constructor
    public DocumentResponseDTO() {
    }

    // ✅ Full constructor (used when returning all document details)
    public DocumentResponseDTO(Long id, String fileName, String content, String summary, LocalDateTime createdAt) {
        this.id = id;
        this.fileName = fileName;
        this.content = content;
        this.summary = summary;
        this.createdAt = createdAt;
    }

    // ✅ Partial constructor (optional - if needed by ElasticService)
    public DocumentResponseDTO(Long id, String fileName, String summary, LocalDateTime createdAt) {
        this.id = id;
        this.fileName = fileName;
        this.summary = summary;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
