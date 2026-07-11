package com.safenews.api.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PagedResponseDTO<T>(
        List<T> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {
    public PagedResponseDTO(Page<T> page) {
        this(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
