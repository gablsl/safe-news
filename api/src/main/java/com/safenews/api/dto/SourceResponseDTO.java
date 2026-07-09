package com.safenews.api.dto;

import com.safenews.api.model.Language;
import com.safenews.api.model.Source;
import com.safenews.api.model.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public record SourceResponseDTO(
        UUID id,
        String name,
        Language language,
        Status status,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public SourceResponseDTO(Source source) {
        this(
                source.getId(),
                source.getName(),
                source.getLanguage(),
                source.getStatus(),
                source.getDescription(),
                source.getCreatedAt(),
                source.getUpdatedAt()
        );
    }
}
