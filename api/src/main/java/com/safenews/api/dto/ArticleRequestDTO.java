package com.safenews.api.dto;

import com.safenews.api.model.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ArticleRequestDTO(
        @NotNull(message = "Source ID is required")
        UUID sourceId,

        @NotBlank(message = "Title is required")
        String title,

        @NotNull(message = "Language is required")
        Language language,

        @NotBlank(message = "URL is required")
        String url,

        String description,

        OffsetDateTime publishedAt
) {}
