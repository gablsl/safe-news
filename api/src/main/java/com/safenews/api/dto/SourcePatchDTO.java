package com.safenews.api.dto;

import com.safenews.api.model.Language;
import org.openapitools.jackson.nullable.JsonNullable;

public record SourcePatchDTO(
        JsonNullable<String> name,
        JsonNullable<Language> language,
        JsonNullable<String> url,
        JsonNullable<String> description,
        JsonNullable<Boolean> active
) {}
