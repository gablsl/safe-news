package com.safenews.api.controller;

import com.safenews.api.dto.SourceRequestDTO;
import com.safenews.api.dto.SourceResponseDTO;
import com.safenews.api.service.SourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
    public ResponseEntity<List<SourceResponseDTO>> getAll() {
        return ResponseEntity.ok(sourceService.getAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<SourceResponseDTO>> getEnabledSources() {
        return ResponseEntity.ok(sourceService.getEnabledSources());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SourceResponseDTO> getById(@PathVariable UUID id) {
        return sourceService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        sourceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
