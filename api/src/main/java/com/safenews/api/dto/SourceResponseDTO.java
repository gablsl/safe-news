package com.safenews.api.dto;

import com.safenews.api.model.Language;
import com.safenews.api.model.Source;


import java.time.OffsetDateTime;
import java.util.UUID;

public record SourceResponseDTO(
        UUID id,
        String name,
        Language language,
        String url,
        boolean active,
        String description,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public SourceResponseDTO(Source source) {
        this(
                source.getId(),
                source.getName(),
                source.getLanguage(),
                source.getUrl(),
                source.isActive(),
                source.getDescription(),
                source.getCreatedAt(),
                source.getUpdatedAt()
        );
    }
}
