package com.safenews.api.dto;

import com.safenews.api.model.Article;
import com.safenews.api.model.Language;
import com.safenews.api.model.Source;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ArticleResponseDTO(
        UUID id,
        UUID sourceId,
        String title,
        Language language,
        String description,
        OffsetDateTime publishedAt,
        OffsetDateTime createdAt
) {
    public ArticleResponseDTO(Article article) {
        this(
                article.getId(),
                article.getSource().getId(),
                article.getTitle(),
                article.getLanguage(),
                article.getDescription(),
                article.getPublishedAt(),
                article.getCreatedAt()
        );
    }
}
