package com.safenews.api.service;

import com.safenews.api.dto.SourcePatchDTO;
import com.safenews.api.dto.SourceRequestDTO;
import com.safenews.api.dto.SourceResponseDTO;
import com.safenews.api.exception.DuplicateResourceException;
import com.safenews.api.exception.ResourceNotFoundException;
import com.safenews.api.model.Source;
import com.safenews.api.repository.SourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SourceService {
    private final SourceRepository sourceRepository;

    @Transactional
    public SourceResponseDTO create(SourceRequestDTO dto) {
        if (sourceRepository.existsByUrl(dto.url())) {
            throw new DuplicateResourceException("Source with URL " + dto.url() + "already exists");
        }

        return new SourceResponseDTO(sourceRepository.save(new Source(dto)));
    }

    @Transactional(readOnly = true)
    public Page<SourceResponseDTO> getAll(Boolean active, Pageable pageable) {
        if (active != null) {
            return sourceRepository.findByActive(active, pageable)
        .       map(SourceResponseDTO::new);
        }

        return sourceRepository.findAll(pageable)
            .map(SourceResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public SourceResponseDTO getById(UUID id) {
        return sourceRepository.findById(id)
                .map(SourceResponseDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Source not found with ID " + id));
    }

    @Transactional
    public SourceResponseDTO update(UUID id, SourcePatchDTO dto) {
        Source source = sourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Source not found with ID " + id));

        if (dto.url().isPresent()) {
            String newUrl = dto.url().orElse(null);
            if (newUrl != null && !source.getUrl().equals(newUrl) && sourceRepository.existsByUrl(newUrl)) {
                throw new DuplicateResourceException("Another source with URL " + newUrl + " already exists.");
            }
        }

        source.patchFromDTO(dto);

        return new SourceResponseDTO(sourceRepository.save(source));
    }

    @Transactional
    public void delete(UUID id) {
        if (!sourceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Source not found with ID " + id);
        }

        sourceRepository.deleteById(id);
    }
}
