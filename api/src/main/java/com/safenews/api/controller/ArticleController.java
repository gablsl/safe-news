package com.safenews.api.controller;

import com.safenews.api.dto.ArticleRequestDTO;
import com.safenews.api.dto.ArticleResponseDTO;
import com.safenews.api.dto.PagedResponseDTO;
import com.safenews.api.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<ArticleResponseDTO> create(@RequestBody @Valid ArticleRequestDTO dto) {
        return ResponseEntity.ok(articleService.create(dto));
    }

    @GetMapping
    public ResponseEntity<PagedResponseDTO<ArticleResponseDTO>> getAll(
            @PageableDefault(size = 15, sort = "publishedAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        return ResponseEntity.ok(new PagedResponseDTO<>(articleService.getAll(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(articleService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        articleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
