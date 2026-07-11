package com.safenews.api.controller;

import com.safenews.api.dto.PagedResponseDTO;
import com.safenews.api.dto.SourcePatchDTO;
import com.safenews.api.dto.SourceRequestDTO;
import com.safenews.api.dto.SourceResponseDTO;
import com.safenews.api.service.SourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/sources")
@RequiredArgsConstructor
public class SourceController {
    private final SourceService sourceService;

    @PostMapping
    public ResponseEntity<SourceResponseDTO> create(@RequestBody @Valid SourceRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sourceService.create(dto));
    }

    @GetMapping
    public ResponseEntity<PagedResponseDTO<SourceResponseDTO>> getSources(
            @RequestParam(required = false) Boolean active,
            @PageableDefault(size = 15, sort = "name") Pageable pageable
    ) {
        Page<SourceResponseDTO> response = sourceService.getAll(active, pageable);
        return ResponseEntity.ok(new PagedResponseDTO<>(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SourceResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(sourceService.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SourceResponseDTO> update(
            @PathVariable UUID id,
            @RequestBody SourcePatchDTO dto
    ) {
        return ResponseEntity.ok(sourceService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        sourceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
