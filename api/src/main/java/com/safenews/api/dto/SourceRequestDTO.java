package com.safenews.api.dto;

import com.safenews.api.model.Language;
import com.safenews.api.model.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SourceRequestDTO(
        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Language is required")
        Language language,

        @NotNull(message = "Status is required")
        Status status,

        String description
) {}
