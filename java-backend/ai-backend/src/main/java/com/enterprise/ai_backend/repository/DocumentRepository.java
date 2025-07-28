package com.enterprise.ai_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enterprise.ai_backend.model.DocumentEntity;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {

    // ✅ UPDATED: Return a list to avoid IncorrectResultSizeDataAccessException
    List<DocumentEntity> findByFileName(String fileName);

    // 🔎 Search by fileName, content, or summary (case-insensitive, partial match)
    List<DocumentEntity> findByFileNameContainingIgnoreCaseOrContentContainingIgnoreCaseOrSummaryContainingIgnoreCase(
        String fileName, String content, String summary
    );
}
